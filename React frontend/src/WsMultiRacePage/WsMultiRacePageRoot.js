import React, {useState, useEffect, useRef} from 'react';



import ContentContainer from '../pageRace/ContentContainer';
import ResultInfo from '../pageRace/ResultInfo';
import ProgressBar from '../pageRace/ProgressBar';
import {Link, useParams} from 'react-router-dom';



export default function WebsocketTestPageRoot() {
  const additionalLength = 6;
  
  const [allQuotes, setAllQuotes] = useState();
  const [quote, setQuote] = useState();
  const [quoteMeta, setQuoteMeta] = useState();
  const [input, setInput] = useState("");
  const [targetIndex, setTargetIndex] = useState();
  const [targetLength, setTargetLength] = useState();
  const [targetLetterStates, setTargetLetterStates] = useState();
  const [letterIndex, setLetterIndex] = useState();
  const [hiddenElementFlags, setHiddenElementFlags] = useState({race: false, result: true});
  const [raceProgess, setRaceProgress] = useState();
  const [userInfo, setUserInfo] = useState();
  const [placement, setPlacement] = useState();
  const [raceCountdown, setRaceCountdown] = useState();
  

  const raceStatus = useRef();
  const charAccumulator = useRef(0);
  const timer = useRef({startTime: undefined, timeUsed: undefined});
  const inputElementRef = useRef();
  const raceStartTimestamp = useRef();

  const token = localStorage.getItem("token");
  const socket = useRef();

  

  const {roomId} = useParams();


  const refAllQuotes = useRef();
  useEffect(() => {
    refAllQuotes.current = allQuotes;
  }, [allQuotes]);

  const refQuoteMeta = useRef();
  useEffect(() => {
    refQuoteMeta.current = quoteMeta;
  }, [quoteMeta]);

  const refUserInfo = useRef();
  useEffect(() => {
    refUserInfo.current = userInfo;
  }, [userInfo]);




  const commandFnMap = {
    "startTest": startTest,
    "startTimer": processStartTime,
    "finishTest": finishTest,
    "progressUpdate": progressUpdate,
    "primeTest": primeTest,
  };


  /**
   * TODOs:
   * 
   * refactor local and ws race code, abstract where possible
   * change the server start time to sendtoall eventually
   * prevent players from joining the lobby mid-race
   * prevent players from starting a new test until all players are finished, or just kick the players that have finished out of the room
   *  or some other solutions like that.
   * 
   * start countdown for a race once at least 2 players joined
   * limit the player count to 5 or 
   * 
   * add a "lobby" page that is displayed when a player first join
   *  the player would have to confirm to join the next race
   * 
   * check for dead connections constantly, or maybe whenever a new player registers
   * 
   * when the client attempt to reopen a closed ws connection, redirect them to a new connection or something...
   */
  

  
  // There are, and always will be reasons to believe in the pitiful illusions that are consciouness and freewill

  // stuff and stuff


  function commandFn(e) {
    const msg = JSON.parse(e.data);
    let command = msg.command;
    console.log(command);
    let content = msg.content

    let targetFn = commandFnMap[command];
    if (targetFn) {
      targetFn.call(null, content);
    }
  }


  function sendMessage(message) {
    if (socket.current) {
      socket.current.send(message);
    }
  }
  
  

  useEffect(() => {
    socket.current = new WebSocket("ws://localhost:8080/race/"+roomId);

    socket.current.onopen = () => {
      console.log("connection opened");
      if (userInfo) {
        sendMessage(JSON.stringify({"command": "register", "content": {"userId": userInfo.id, "username": userInfo.username}}))
        console.log("register request sent")
      }
    }
    
    socket.current.onclose = (e) => {
      console.log("connection closed");
    }
    
    socket.current.onmessage = (e) => {
      commandFn(e);
    }
    
    socket.current.onerror = (e) => {
      console.log("an error has occurred " + e.data);
    }
    
  
    
    fetch("http://localhost:8080/log/quote")
      .then(result => result.json())
      .then(data => {
        setAllQuotes(data); 
      })
      
      .catch(err => {
        let errorQuoteMeta = {id: -1, content: 'Oh no, looks like we are experiencing some technical difficulties. Please be patient with our technical team. In the meantime, we offer you this error message to play with!', length: 172, source: 'The technical team'};
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
        let tempGuestId = generateUUID();
        const guestData = {id: tempGuestId.toString(), username: "Guest", average: 0, races: 0};
        setUserInfo(guestData);
        sendMessage(JSON.stringify({"command": "register", "content": {"userId": guestData.id, "username": guestData.username}}))
      }
    })
    .then(data => {
      if (data) {
        setUserInfo(data);
        sendMessage(JSON.stringify({"command": "register", "content": {"userId": data.id, "username": data.username}}))
      }
    })
    .then().catch(err => {
      console.error(err);
    })


  }, []);


  function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      let r = Math.random() * 16 | 0,
          v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
}








  function progressUpdate(content) {
    setRaceProgress(content["progress"]);
  }

  
  function processStartTime(content) {
    timer.current.startTime = content["timestamp"];
  }

  
  function primeTest(content) {
    raceStartTimestamp.current = parseInt(content["startTime"]);
    timer.current.startTime = raceStartTimestamp.current;
    let tempCountdown = Math.round((raceStartTimestamp.current - Date.now()) / 1000);
    setRaceCountdown(tempCountdown);
  }


  useEffect(() => {
    setInterval(() => {
      let tempCountdown = Math.round((raceStartTimestamp.current - Date.now()) / 1000);
      setRaceCountdown(tempCountdown);
    }, 100);
  }, [raceCountdown])
  


  function startTest(content) {
    let selector = content["selector"];
    let initQuote = refAllQuotes.current[selector].content.split(" ");
    timer.current.startTime = undefined;
    timer.current.timeUsed = undefined;

    toggleElementToForeground("race");
    requestAnimationFrame(() => {
      inputElementRef.current.focus();
    });



    inputReset(0, initQuote);
    setQuote(initQuote);
    setQuoteMeta(refAllQuotes.current[selector]);
    setRaceProgress(content["progress"]);

    raceStatus.current = "inProgress";
  }



  function typoCheck() {
    for (let i=0; i<targetLetterStates.length; i++) {
      if (targetLetterStates[i] !== "correct" && targetLetterStates[i] !== "unused") {
        return false;
      }
    }
    return true;
  }


  /**
   * 
   * 
   * */ 
  


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
    } else {
      charAccumulator.current += quoteParam[newIndex - 1].length + 1;
      let progressInit = Math.round(charAccumulator.current / quoteMeta.length * 100) + "%";
      sendMessage(JSON.stringify({"command": "progressUpdate", "content": {"progress": progressInit}}))
    }
  
  }

  


  function inputUpdate(e) {
    if (!quote || !quoteMeta) {
      return;
    }

    let {value: inputValue} = e.target;
    let {data, inputType} = e.nativeEvent;


    if (!timer.current.startTime) {
      // note during refactoring period:
      //   this message won't be sent if the test was already primed, so the startTime won't be undefined
      sendMessage(JSON.stringify({"command": "getStartTimer", "content": null}));
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
      sendMessage(JSON.stringify({"command": "progressUpdate", "content": {"progress": "100%"}}))
      sendMessage(JSON.stringify({"command": "endTest", "content": null}));
    }
  }





  function toggleElementToForeground(targetElement) {
    let tempFlag = {};
    for (var key in hiddenElementFlags) {
      tempFlag[key] = !(targetElement === key);
    }
    setHiddenElementFlags(tempFlag);
  }



  function finishTest(content) {
    toggleElementToForeground("result");
    setPlacement(content["placement"]);

    let endTime;
    if (timer.current.startTime) {
      timer.current.timeUsed = (content["timestamp"] - timer.current.startTime) /1000;
      endTime = content["dateTime"];
    }


    if (token) {
      let wpm = Math.round(refQuoteMeta.current.length / (timer.current.timeUsed/60) /5 *100) / 100;

      refUserInfo.current.races++;
      refUserInfo.current.average = Math.round(((refUserInfo.current.average * (refUserInfo.current.races-1) + wpm) / refUserInfo.current.races) *100) /100;
      let postRaceUpdateReq = new Request("http://localhost:8080/log/user/"+refUserInfo.current.id, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": token, 
        },
        body: JSON.stringify({"id": refUserInfo.current.id, "username": refUserInfo.current.username, "average": refUserInfo.current.average, "races": refUserInfo.current.races})
      });

      fetch(postRaceUpdateReq).then(res => {
        if (res.ok) {
          return res.json(); 
        } else if (res.status === 401) {
          localStorage.removeItem("token");
          let tempGuestId = generateUUID();
          const guestData = {id: tempGuestId.toString(), username: "Guest", average: 0, races: 0};
          setUserInfo(guestData);
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
        body: JSON.stringify({"userId": refUserInfo.current.id, "quoteId": refQuoteMeta.current.id, "time": timer.current.timeUsed, "wpm": wpm, "date": endTime})
      });

      fetch(createRecordReq);

    }

  }



  return (
    <div>
      <div className="flex justify-end mr-8">
        { !userInfo ? <Link to="/login" className="text-xl rounded-full bg-indigo-200 px-4 py-1 shadow-sm">Login here</Link> :
          (
            !Number.isInteger(Number(userInfo.id)) ? 
            <Link to="/login" className="text-xl rounded-full bg-indigo-200 px-4 py-1 shadow-sm">Login here</Link> : 
            <div className="flex-col justify-start items-start">
              <div className="text-xl text-indigo-500 font-semibold">{userInfo.username}</div>
              <div className="text-base font-semibold">
                wpm: <span className="font-normal">{userInfo.average}</span>
              </div>
            </div>
          )
        }
      </div>
      

      {/* todo: racecountdown message is not yet implemented, and also the display condistion for this dom node should be refined */}
      {timer.current.startTime && <div>{raceCountdown.toString()}</div>}
      {raceProgess && <ProgressBar playerProgress={raceProgess} localUserId={userInfo.id}/>}
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
        <ResultInfo quoteMeta={quoteMeta} time={timer.current.timeUsed ?  timer.current.timeUsed : -1} placement={placement}/>
      </div>

      {allQuotes && <button className="h-0 w-0" onFocus={() => sendMessage(JSON.stringify({"command": "startTest", "content": null}))}/>}


    </div>
  )
}