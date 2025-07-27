import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import YouTube from 'react-youtube';
import '../../_style/lecture/lecture.css'

function VideoPlayer() {
  const location = useLocation();
  const videoId = new URLSearchParams(location.search).get('v');

  const [video, setVideo] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const opts = {
    playerVars: {
      autoplay: 1,
    },
  };

   useEffect(() => {
    if (!videoId) return;
    
    setLoading(true);
    setError(null);

    fetch(`/api/search?query=${encodeURIComponent(videoId)}`)
      .then(res => {
        if (!res.ok) throw new Error('영상 정보를 불러오는 데 실패했습니다');
        return res.json();
      })
      .then(data => setVideo(data))
      .catch(err => setError(err.message))
      .finally(() => setLoading(false));
  }, [videoId]);

  return (
    <>
      <div className="videoPlayer">
        {error && <div className="text-danger">에러: {error}</div>}
        {loading && <div>영상 정보를 불러오는 중입니다...</div>}
        {video && video.map((lecture) => (         
          <YouTube
            videoId={videoId}
            opts={opts}
            onReady={(e) => console.log("YouTube Player 준비됨", e)}
            onError={(e) => console.error("YouTube Player 에러", e)}
          />
        ))}
      </div>
    </>

  );
}

export default VideoPlayer;
