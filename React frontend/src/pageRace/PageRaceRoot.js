import {useState, useEffect, useRef} from 'react';
import ContentContainer from './ContentContainer';
import ResultInfo from './ResultInfo';
import ProgressBar from './ProgressBar';
import {Link} from 'react-router-dom';

const additionalLength = 6;



function App() {

  const [allQuotes, setAllQuotes] = useState();
  const [quote, setQuote] = useState();
  const [quoteMeta, setQuoteMeta] = useState();
  const [input, setInput] = useState("");
  const [targetIndex, setTargetIndex] = useState();
  const [targetLength, setTargetLength] = useState();
  const [targetLetterStates, setTargetLetterStates] = useState();
  const [letterIndex, setLetterIndex] = useState();
  const [hiddenElementFlags, setHiddenElementFlags] = useState({race: false, result: true});
  const [progress, setProgress] = useState("0%");
  const [userInfo, setUserInfo] = useState({id: -1, username: "Guest", average: 0, races: 0});

  const charAccumulator = useRef(0);
  const timer = useRef({startTime: undefined, timeUsed: undefined});
  const inputElementRef = useRef();

  const token = localStorage.getItem("token");

  useEffect(() => {
    
    fetch("http://localhost:8080/log/quote")
      .then(result => result.json())
      .then(data => {
        startTest(data);
        setAllQuotes(data);
      })
      
      .catch(err => {
        let errorQuoteMeta = {id: -1, content: 'Oh no, looks like we are experiencing some technical difficulties. Please be patient with our technical team. In the meantime, we offer you this error message to play with!', length: 172, source: 'The technical team'};
        startTest([errorQuoteMeta]);
        setAllQuotes([errorQuoteMeta]);
      });
    
    let userInfoReq = new Request("http://localhost:8080/auth/userInfo", {
      method: "GET",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      }
    });

    fetch(userInfoReq)
    .then(res => {
      if (res.ok) {
        return res.json();
      } else {
        setUserInfo({id: -1, username: "Guest", average: 0, races: 0});
      }
    })
    .then(data => {
      if (data) {
        setUserInfo(data);
      }
    })
    .then().catch(err => {
      console.error(err);
    })
  }, []);

  
  function startTest(data = allQuotes) {
    let selector = getRandomInt(0, data.length); 
    let initQuote = data[selector].content.split(" ");
    timer.current.startTime = undefined;
    timer.current.timeUsed = undefined;
    toggleElementToForeground("race");
    requestAnimationFrame(() => {
      inputElementRef.current.focus();
    })


    inputReset(0, initQuote);
    setQuote(initQuote);
    setQuoteMeta(data[selector]);
  }


  function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min) ) + min;
  }



  function typoCheck() {
    for (let i=0; i<targetLetterStates.length; i++) {
      if (targetLetterStates[i] !== "correct" && targetLetterStates[i] !== "unused") {
        return false;
      }
    }
    return true;
  }



  function inputReset(newIndex, quoteParam = quote) {
    let wordLength = quoteParam[newIndex].length;
    const initArr = new Array(wordLength + additionalLength);
    for (let i=0; i<wordLength; i++) {
      initArr[i] = "initial";
    }
    for (let i=wordLength; i<wordLength+additionalLength; i++) {
      initArr[i] = "unused";
    }
    setInput("");
    setLetterIndex(0);
    setTargetIndex(newIndex);
    setTargetLength(wordLength);
    setTargetLetterStates(initArr);
    
    if (newIndex === 0) {
      charAccumulator.current = 0;
      setProgress("0%");
    } else {
      charAccumulator.current += quoteParam[newIndex - 1].length + 1;
      setProgress(Math.round(charAccumulator.current / quoteMeta.length * 100) + "%");
    }
    
  }



  function inputUpdate(e) {
    if (!quote || !quoteMeta) {
      return;
    }

    let {value: inputValue} = e.target;
    let {data, inputType} = e.nativeEvent;


    if (!timer.current.startTime) {
      timer.current.startTime = new Date().getTime();
    }

    switch(inputType) {
      case "insertText":
        if (data === " " && typoCheck()) {
          inputReset(targetIndex+1);
          return;
        }
  
        if (letterIndex >= targetLength + additionalLength) {
          return;
        }
  
        if (letterIndex >= targetLength) {
          targetLetterStates[letterIndex] = "incorrectPadding";
          data = (data === " " ? "_" : data);
          quote[targetIndex] = quote[targetIndex] + data;
        } else if (data === quote[targetIndex][letterIndex]) {
          targetLetterStates[letterIndex] = "correct";
        } else {
          targetLetterStates[letterIndex] = "incorrect";
        }
  
        setLetterIndex(letterIndex + 1);
        break;
      

      case "deleteContentBackward":
      case "deleteWordBackward":
        if (letterIndex === 0) {
          return;
        } 

        let remainingLength = inputValue.length;
        
        quote[targetIndex] = quote[targetIndex].substring(0, Math.max(targetLength, remainingLength));
        for (let i=remainingLength; i<letterIndex; i++) {
          if (i >= targetLength) {
            targetLetterStates[i] = "unused";
          } else {
            targetLetterStates[i] = "initial";
          }
        }

        setLetterIndex(remainingLength);
        break;


      default:
        return;
    }


    setInput(inputValue);
    if (targetIndex === quote.length-1 && letterIndex === targetLength-1 && typoCheck()) {
      finishRace();
    }
  }



  function toggleElementToForeground(targetElement) {
    for (var key in hiddenElementFlags) {
      hiddenElementFlags[key] = !(targetElement === key);
    }
  }



  function finishRace() {
    console.log("You've finished the test!!");
    toggleElementToForeground("result");
    let endTime = new Date();
    timer.current.timeUsed = (endTime.getTime() - timer.current.startTime) /1000;
    setProgress("100%");
    
    if (token) {
      let wpm = Math.round(quoteMeta.length / (timer.current.timeUsed/60) /5 *100) / 100;
      userInfo.races++;
      userInfo.average = Math.round(((userInfo.average * (userInfo.races-1) + wpm) / userInfo.races) *100) /100;
      let postRaceUpdateReq = new Request("http://localhost:8080/log/user/"+userInfo.id, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": token,
        },
        body: JSON.stringify({"id": userInfo.id, "username": userInfo.username, "average": userInfo.average, "races": userInfo.races})
      });

      fetch(postRaceUpdateReq).then(res => {
        if (res.ok) {
          return res.json(); 
        } else if (res.status === 401) {
          localStorage.removeItem("token");
          setUserInfo({id: -1, username: "Guest", average: 0, races: 0});
          console.log("login required")
        }
      })
      
      .catch(err => {
        console.error("Fetch error"+ err);
      });



      let createRecordReq = new Request("http://localhost:8080/log/record", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": token,
        },
        body: JSON.stringify({"userId": userInfo.id, "quoteId": quoteMeta.id, "time": timer.current.timeUsed, "wpm": wpm, "date": endTime})
      });

      fetch(createRecordReq).then(res => {

      });

    }

  }


  return (
    <div className="App" >
      <div className="flex justify-end mr-8">
        {
          userInfo.id === -1 ? 
          <Link to="/login" className="text-xl rounded-full bg-indigo-200 px-4 py-1 shadow-sm">Login here</Link> : 
          <div className="flex-col justify-start items-start">
            <div className="text-xl text-indigo-500 font-semibold">{userInfo.username}</div>
            <div className="text-base font-semibold">
              wpm: <span className="font-normal">{userInfo.average}</span>
            </div>
          </div>
        }
      </div>
      
      <ProgressBar playerProgress={[{username: userInfo.username, color: "#c197db", progress: progress}]}/>
      <div id="pageRace" className={"mt-24 mx-40" + (hiddenElementFlags.race ? " hidden" : "")}>
        
        <ContentContainer quote={quote} targetIndex={targetIndex} targetLetterStates={targetLetterStates}/>

        <div className="mt-10 px-20">
          <input type="text" autoFocus={true} value={input}
            className="min-w-32 w-full border-2 rounded-md border-stone-400 focus:outline-indigo-300" 
            onChange={(e) => inputUpdate(e)} ref={inputElementRef}>
          </input>
        </div>
        
      </div>

      
      
      <div id="pageResult" className={"mt-24 mx-40" + (hiddenElementFlags.result ? " hidden" : "")}>
        <ResultInfo quoteMeta={quoteMeta} time={timer.current.timeUsed ?  timer.current.timeUsed : -1} placement={1}/>
      </div>

      <button className="h-0 w-0" onFocus={allQuotes ? () => startTest() : undefined}/>
    </div>
  );
}

export default App;
