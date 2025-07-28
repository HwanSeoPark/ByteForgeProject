import { useState, useEffect } from 'react';
import { useNavigate, Route, Routes } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import { Typography, Box } from '@mui/material';
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import IconButton from '@mui/material/IconButton';
import SearchResult from './SearchResult';
import '../../_style/lecture/lecture.css'

function VideoSearchBar() {
  const [query, setQuery] = useState('');
  const [videos, setVideos] = useState([]);
  const navigate = useNavigate();

  const handleSearch = () => {
    if (!query.trim()) return;
    navigate(`/search?query=${encodeURIComponent(query)}`);
    console.log("검색 요청 실행");
  };

  useEffect(() => {
    fetch('/api/search?query=JAVA 강의,Spring 강의') // Search Default query
      .then(res => {
        if (!res.ok) throw new Error('API 호출 실패');
        return res.json();
      })
      .then(data => setVideos(data))
  }, [query]);

  const handleCardClick = (videoId) => {
    navigate(`/video?v=${encodeURIComponent(videoId)}`);
  };

  return (
    <section className='videoSearchArea'>
        <Box sx={{ mt: 4, width: '100%', maxWidth: 600, mx: 'auto' }}>
            <TextField
            fullWidth
            placeholder="검색어 입력"
            variant="outlined"
            autoComplete="off" 
            size='small'
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={(e) => { if (e.key === 'Enter') handleSearch(); }}
            InputProps={{
                endAdornment: (
                <IconButton onClick={handleSearch} edge="end" aria-label="search">
                    <SearchOutlinedIcon />
                </IconButton>
                ),
            }}
            />
          </Box>
          {query ? (
            <></>
          ) : (
            <section className="DefaultArea row mt-4">
              {videos.map((video) => (
                  <div
                      key={video.videoId}
                      className="col-12 col-sm-6 col-md-4 col-lg-3 mb-4"
                      onClick={() => handleCardClick(video.videoId)}
                      style={{ cursor: 'pointer' }}
                  >
                      <div className="card h-100">
                          <img
                              src={video.videoThumbnailUrl}
                              alt={video.videoTitle}
                              className="card-img-top"
                          />
                          <div className="card-body d-flex flex-column">
                              <Typography variant="subtitle1" className="card-title"
                              sx={{
                              display: '-webkit-box',
                              WebkitLineClamp: 2,
                              WebkitBoxOrient: 'vertical',
                              overflow: 'hidden',
                              textOverflow: 'ellipsis',
                              whiteSpace: 'normal',
                              }}>{video.videoTitle}</Typography>
                              <Typography variant="body2" className="text-muted" sx={{ fontSize: '0.9em',  mt: 'auto' }}>
                              게시일: {new Date(video.videoPublishedAt).toLocaleDateString()}
                              </Typography>
                          </div>
                      </div>
                  </div>
              ))}
            </section>
          )}
          
        <Routes>
             <Route path='*' element={<SearchResult/>}/>
        </Routes>
    </section>
  );
}

export default VideoSearchBar;
