import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import FindID from './component/login/findID';
import Logins from './component/login/login';
import Register from './component/login/register';
import NoticeFrame from './component/noticeboard/Frame';
import 'bootstrap/dist/css/bootstrap.min.css';
import AdminPage from './component/admin/admin';
import AdminLogin from './component/admin/adminLogin';
import VideoPlayer from './component/lecture/VideoPlayer';

function App() {

  return (
    <BrowserRouter>
      <div className='mainArea'>
        <Routes>
          <Route path='/login' element={<Logins />} />
          <Route path='/register' element={<Register />} />
          <Route path='/findid' element={<FindID />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/admin/login" element={<AdminLogin />} />
          <Route path='*' element={<NoticeFrame />} />
          <Route path='/video' element={<VideoPlayer/>}/>
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
