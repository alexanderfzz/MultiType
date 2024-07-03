import React, {useState} from 'react'


export default function LoginPageRoot(props) {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginFailed, setLoginFailed] = useState(false);

  function handleSubmit(e) {
    e.preventDefault();
    let loginRequest = new Request("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({username: username, password: password})
    });

    fetch(loginRequest).then(res => {
      if (res.ok) {
        return res.json();
      } else {
        setLoginFailed(true);
      }
    }).then(data => {
      if (!data) {
        localStorage.removeItem("token");
      } else if (data.token) {
        localStorage.setItem("token", data.token);
        window.location.href = "/";
      }
    });

    
  }



  return (
    <div className="flex justify-center items-center h-full">
      <form onSubmit={handleSubmit} className="h-4/6 w-2/5 rounded-lg shadow-lg flex-col bg-slate-300 text-stone-800 relative">
        <h1 className="text-5xl text-center m-16">Login</h1>
        <h2 className={(loginFailed ? "": "hidden ")+"text-sm h-auto w-5/6 mx-auto text-red-800 font-semibold mb-6"}>Login failed. Please make sure the credentials are correct.</h2>
        <label htmlFor="username" className="text-xl mx-auto mb-8 pb-2 w-2/3 block placeholder-slate-500">
          Username: 
          <input id="username" name="username" type="text" placeholder="YourUsername" onChange={(e) => setUsername(e.target.value)}
            className="outline-none border-b-2 bg-transparent block w-full" autoComplete="off"/>
        </label>
        
        <label htmlFor="password" className="text-xl mx-auto mb-8 pb-2 w-2/3 block placeholder-slate-500">
          Password: 
          <input id="password" name="password" type="password" placeholder="********" onChange={(e) => setPassword(e.target.value)}
            className="outline-none border-b-2 bg-transparent block w-full"/>
        </label>
        
        <button type="submit" className="mx-auto w-2/3 p-1.5 border text-xl border-stone-800 rounded-full block">Login</button>
      </form>
    </div>
  )
}
