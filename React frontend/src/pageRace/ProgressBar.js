import React from 'react'

export default function ProgressBar(props) {
  const {playerProgress} = props;

  return (
    <div id="progressBar" className="flex-col mt-12 mx-20">
      {
        playerProgress.map(({username, color, progress}, index) => {
          return (
            <div key={index} className="flex h-4">
              <div>{username}</div>
              <div className="h-full w-10/12 rounded-full bg-gray-200 ml-4">
                <div className="h-full rounded-full" style={{width: progress, backgroundColor: color}}/>
              </div>
            </div>
          );
          
        })
      }
    </div>
  )
}
