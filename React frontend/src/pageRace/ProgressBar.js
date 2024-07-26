import React from 'react'

export default function ProgressBar(props) {
  const {playerProgress, localUserId} = props;


  return (
    <div id="progressBar" className="flex-col mt-12 mx-20">
      {
        Object.entries(playerProgress).map(([userId, userInstance], index) => {
          let playerColor = userId === localUserId ? "#c197db" : "#ff5555";
          return (
            <div key={index} className="flex h-4 mt-6">
              <div>{userInstance["username"]}</div>
              <div className="h-full w-10/12 rounded-full bg-gray-200 ml-4">
                <div className="h-full rounded-full" style={{width: userInstance["progress"], backgroundColor: playerColor}}/>
              </div>
            </div>
          );
          
        })
      }
    </div>
  )
}





// export default function ProgressBar(props) {
//   const {playerProgress} = props;

//   return (
//     <div id="progressBar" className="flex-col mt-12 mx-20">
//       {
//         playerProgress.map(({username, color, progress}, index) => {
//           return (
//             <div key={index} className="flex h-4 mt-6">
//               <div>{username}</div>
//               <div className="h-full w-10/12 rounded-full bg-gray-200 ml-4">
//                 <div className="h-full rounded-full" style={{width: progress, backgroundColor: color}}/>
//               </div>
//             </div>
//           );
          
//         })
//       }
//     </div>
//   )
// }
