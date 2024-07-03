import React from 'react'

export default function ResultInfo(props) {
  let {quoteMeta, time, placement} = props;
  if (!quoteMeta) {
    return <div></div>;
  }
  const wpm = Math.round(quoteMeta.length / (time/60) /5 *100) / 100;
  return (
    <div id="resultInfo"
      className="bg-indigo-100 rounded-2xl px-6 py-3 drop-shadow-xl flex">
      <div id="primaryStats" className="ml-8 flex-col align-middle justify-center h-full">
        <div id="wpmStat" className="flex-col">
          <div className="text-pink-700 font-bold text-2xl">wpm</div>
          <h1 className=" text-7xl text-neutral-700 font-black">{wpm}</h1>
        </div>
      </div>

      <div className="ml-12 flex-col">
        <div id="quoteOrigin">
          <div className="font-bold text-pink-700">You just typed a quote from {quoteMeta.source}.</div>
          <div>"{quoteMeta.content}"</div>
        </div>

        <div id="secondaryStats" className="text-pink-700 font-bold flex mt-8">
          <span className="ml-4">
            <div>characters</div>
            <h1 className="text-3xl text-neutral-700">{quoteMeta.length}</h1>
          </span>
          <span className="ml-12">
            <div>time</div>
            <h1 className="text-3xl text-neutral-700">{Math.round(time)}s</h1>
          </span>
          <span className={"ml-12 "+(!placement ? "hidden" : "")}>
            <div>placement</div>
            <h1 className="text-3xl text-neutral-700">{placement ?? placement}</h1>
          </span>
        </div>
      </div>
      
      

    </div>
  )
}
