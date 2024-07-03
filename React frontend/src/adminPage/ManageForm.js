import React from 'react'

export default function ManageForm(props) {

  const token = localStorage.getItem("token");

  const {manageType, data, requiredFields, updateData} = props;


  function additionSubmit(e) {
    e.preventDefault();
    let addRequest = new Request("http://localhost:8080/log/"+manageType, {
      method: "PUT",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
      body: JSON.stringify(requiredFields),
    });

    fetch(addRequest).then(res => {
      if (res.ok) {
        updateData(manageType);
      } else if (res.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login";
      }
    })
    .catch(err => {
      console.error(err);
    });

  }

  function updateSubmit(e, id, submittedData) {
    e.preventDefault();
    let updateRequest = new Request("http://localhost:8080/log/"+manageType+"/"+id, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
      body: JSON.stringify(submittedData),
    });

    fetch(updateRequest).then(res => {
      if (res.ok) {
        updateData(manageType);
      } else if (res.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login";
      } 
    })
    .catch(err => {
      console.error(err);
    })
  }


  function deleteSubmit(e, id) {
    let deleteRequest = new Request("http://localhost:8080/log/"+manageType+"/"+id, {
      method: "DELETE",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
    });

    fetch(deleteRequest).then(res => {
      if (res.ok) {
        updateData(manageType);
      } else if (res.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login";
      }
    })
    .catch(err => {
      console.error(err);
    })
  }


  return (
    <div>
      {Object.entries(requiredFields).length!==0 && 
        <form className="h-4/6 w-2/5 rounded-lg shadow-lg flex-col bg-slate-300 text-stone-800 relative p-10"
          onSubmit={additionSubmit}>
          {
            Object.entries(requiredFields).map(([key, value], index) => {
              return <label key={index} className="text-xl mb-8 pb-2 w-2/3 block text-wrap">{key}: 
                <input type="text" className="outline-none border-b-2 bg-transparent block w-full"
                  onChange={(e) => {requiredFields[key]=e.target.value;}} value={requiredFields[key]}/>
              </label>
            })
          }
          <button type="submit" className="mt-8 p-0.5 px-2 border text-base border-stone-800 rounded-full block">create</button>
        </form>
      }
      
      <div>
        {
          data &&
          data.map((element, elementIndex) => {

            let submittedData = {...requiredFields};
            // this is problematic, the default values of submitted data should not be undefined unlike requiredFields
            
            return <form key={elementIndex} onSubmit={(e) => updateSubmit(e, element.id)}
              className="mt-10 flex-col bg-stone-200 border rounded-lg p-4">
              {
                requiredFields &&
                Object.entries(element).map(([key, value], attrIndex) => {

                  if (!(key in requiredFields)) {
                    return (
                    <div key={attrIndex} className="text-lg font-semibold">
                      {key}: 
                      <span className="bg-transparent block w-2/3 text-ellipsis text-base font-normal">
                        {value}
                      </span>
                    </div>)
                  } else {
                    return <label key={attrIndex} className="block text-lg font-semibold">
                      {key}: 
                      <input type="text" defaultValue={value} onChange={(e) => {
                        submittedData[key] = e.target.value;
                      }}
                        className="outline-none border-2 rounded-md px-2 border-neutral-400 bg-transparent block w-2/3 text-ellipsis text-base font-normal"></input>
                    </label>
                  }
                })
              }
              <div className="flex">
                <button type="submit" className="mt-8 mr-4 p-0.5 px-2 border text-base border-stone-800 rounded-full block"
                  onClick={(e) => updateSubmit(e, element.id, submittedData)}>Update</button>
                <button type="reset" className="mt-8 mr-4 p-0.5 px-2 border text-base border-stone-800 rounded-full block">Reset</button>
                <button type="button" onClick={(e) => deleteSubmit(e, element.id)} 
                  className="mt-8 mr-4 p-0.5 px-2 border text-base border-stone-800 rounded-full block">Delete</button>
              </div>
              
            </form>
          })
        }
      </div>
      
    </div>
  )
}
