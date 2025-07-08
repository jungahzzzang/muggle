import styled from 'styled-components'

const PosterWrapper = styled.div`
    width: 100%;
    height: 100%;
    border-radius: 10px;
    overflow: hidden;
`

const PosterImg = styled.img`
    // display: block;
    width: 100%;
    height: 100%;
    object-fit: cover;
`

export default function Poster({url, musicalId}) {

    //const kopisImgURL = process.env.REACT_APP_KOPIS_IMG_URL

    return (
        <>
            <PosterWrapper>
                <PosterImg src={url} alt={`poster-${musicalId}`} />
            </PosterWrapper>
        </>
    )
};