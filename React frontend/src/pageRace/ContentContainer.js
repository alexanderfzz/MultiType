import React from 'react';


const constLetterStyle = "text-2xl";
function mapStyle(state) {
  switch (state) {
    case "initial": 
      return "text-slate-400";
    case "correct": 
      return "text-stone-800 bg-emerald-200";
    case "incorrect": 
      return "text-stone-800 bg-red-200";
    case "incorrectPadding":
      return "text-red-500 bg-red-200"
    default:
      return "";
  }
}


export default function ContentContainer(props) {
  const {quote, targetIndex, targetLetterStates} = props;

  return (
    <div id="ContentContainer" className="flex flex-wrap">
      {
        quote &&
        quote.map((word, wordIndex) => {
          return (
            <div className="word ml-3" key={wordIndex}>
              {
                word.split("").map((character, charIndex) => {
                  let dynamicStyle;
                  if (wordIndex < targetIndex) {
                    dynamicStyle = mapStyle("correct");
                  } else if (wordIndex > targetIndex) {
                    dynamicStyle = mapStyle("initial");
                  } else {
                    dynamicStyle = mapStyle(targetLetterStates[charIndex]);
                  }
                  return <span className={"letter "+constLetterStyle+" "+dynamicStyle} key={charIndex}>{character}</span>
                })
              }
            </div>
          )
        })
      }
    </div>
  )
}
