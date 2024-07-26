import React from 'react';
import {Routes, Route, Link} from 'react-router-dom';

import PageRaceRoot from './pageRace/PageRaceRoot';
import LoginPageRoot from './loginPage/LoginPageRoot';
import AdminPageRoot from './adminPage/AdminPageRoot';
import WebsocketTestPageRoot from './WsMultiRacePage/WsMultiRacePageRoot';

export default function App() {
  return (
    <div className="h-screen">
      <Link to="/" className="text-orange-500 text-2xl font-extrabold"> Type Test</Link>
      <Routes>
        <Route path="/" element={<PageRaceRoot/>}/>
        <Route path="/login" element={<LoginPageRoot/>}/>
        <Route path="/admin" element={<AdminPageRoot/>}/>
        <Route path="/ws/:roomId" element={<WebsocketTestPageRoot/>}/>
      </Routes>
    </div>
    
  )
}
