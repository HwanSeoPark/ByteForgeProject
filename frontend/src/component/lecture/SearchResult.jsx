import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { CircularProgress, Typography, Box } from '@mui/material';
import '../../_style/lecture/lecture.css'

function SearchResult() {
  const location = useLocation();
  const navigate = useNavigate();
  const query = new URLSearchParams(location.search).get('query') || '';

  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!query) return;

    setLoading(true);
    setError(null);

    fetch(`/api/search?query=${encodeURIComponent(query)}`)
      .then(res => {
        if (!res.ok) throw new Error('API 호출 실패');
        return res.json();
      })
      .then(data => setVideos(data))
      .catch(err => setError(err.message))
      .finally(() => setLoading(false));
  }, [query]);

  const handleCardClick = (videoId) => {
    navigate(`/video?v=${encodeURIComponent(videoId)}`);
  };

  return (
    <>
        {query ? (
            <section className='searchResultArea'>
                <div className="container mt-4">
                    <Typography variant="h4" gutterBottom align="center">"{query}" 검색 결과</Typography>

                    {loading && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
                            <CircularProgress />
                        </Box>
                    )}
                    {error && <div className="text-danger">에러: {error}</div>}
                    {!loading && !error && videos.length === 0 && <div>검색 결과가 없습니다.</div>}

                    <div className="row mt-4">
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
                    </div>
                </div>
            </section>
        ) : (
            <></>
        )}
    </>
  );
}

export default SearchResult;