import React, {useState, useEffect, useRef} from 'react';
import {Navigate, UNSAFE_DataRouterContext} from 'react-router-dom';

import ManageForm from './ManageForm';

export default function AdminPageRoot() {
  const token = localStorage.getItem("token");


  const [users, setUsers] = useState();
  const [quotes, setQuotes] = useState();
  const [records, setRecords] = useState();
  const [activeForm, setActiveForm] = useState("users");

  const quoteContent = useRef();
  const quoteSource = useRef();

  useEffect(() => {
   fetchData("user");
   fetchData("quote");
   fetchData("record");
  }, []);


  function fetchData(fetchDataType) {
    fetch("http://localhost:8080/log/"+fetchDataType, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": token,
      }
    })
      .then(res => {
        if (res.ok) {
          return res.json(); 
        } else if (res.status === 401) {
          localStorage.removeItem("token");
          window.location.href = "/login";
        }
        
      })
      .then(data => {
        switch (fetchDataType) {
          case "user":
            setUsers(data);
            break;
          case "quote":
            setQuotes(data);
            break;
          case "record":
            setRecords(data);
            break;
          default:
            return;
        }
      })
      
      .catch(err => {
        console.error("Fetch error"+ err);
      });
  }


  if (!token) {
    return (<Navigate to="/login" replace />);
  } 

  return (
    <div>

      <input type="radio" name="tab" id="users" onChange={(e) => setActiveForm(e.target.id)} className="hidden" defaultChecked/>
      <input type="radio" name="tab" id="quotes" onChange={(e) => setActiveForm(e.target.id)} className="hidden"/>
      <input type="radio" name="tab" id="records" onChange={(e) => setActiveForm(e.target.id)} className="hidden"/>
      

      <div className="flex justify-start items-center h-full bg-stone-200 py-3 mt-8">
        <label htmlFor="users">
          <span className={(activeForm==="users"?"bg-slate-400 ":"bg-slate-300 ") + "rounded-full mx-4 text-xl px-4 py-0.5 inline-block"}>users</span>
        </label>
        <label htmlFor="quotes">
          <span className={(activeForm==="quotes"?"bg-slate-400 ":"bg-slate-300 ") + "rounded-full mx-4 text-xl px-4 py-0.5 inline-block"}>quotes</span>
        </label>
        <label htmlFor="records">
          <span className={(activeForm==="records"?"bg-slate-400 ":"bg-slate-300 ") + "rounded-full mx-4 text-xl px-4 py-0.5 inline-block"}>records</span>
        </label>
      </div>


      <div className="mt-6">
        <div className={activeForm !== "users" ? "hidden" : ""}>
          <ManageForm manageType="user" data={users} requiredFields={{"username": undefined, "password": undefined, "email": undefined}} updateData={fetchData}/>
        </div>
        
        <div className={activeForm !== "quotes" ? "hidden" : ""}>
          <ManageForm manageType="quote" data={quotes} requiredFields={{"content": undefined, "source": undefined}} updateData={fetchData}/>
        </div>
        
        <div className={activeForm !== "records" ? "hidden" : ""}>
          <ManageForm manageType="record" data={records} requiredFields={{}} updateData={fetchData}/>
        </div>
      </div>
      
      
    </div>
  );
}
