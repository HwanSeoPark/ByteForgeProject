import { useState } from 'react';
import { useNavigate, Route, Routes } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import IconButton from '@mui/material/IconButton';
import SearchResult from './SearchResult';

import '../../_style/lecture/lecture.css'

function VideoSearchBar() {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  const handleSearch = () => {
    if (!query.trim()) return;
    navigate(`/search?query=${encodeURIComponent(query)}`);
    console.log("검색 요청 실행");
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
        <Routes>
             <Route path='*' element={<SearchResult/>}/>
        </Routes>
    </section>
  );
}

export default VideoSearchBar;
