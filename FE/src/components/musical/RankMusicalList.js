import { useState } from "react";
import styled from "styled-components";
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay, Navigation, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import Loading from "../shared/Loading";
import MusicalItem from "./MusicalItem";

export default function RankMusicalList({data}) {

    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const handleTheaterClick = async (item) => {
        const theaterId = item.theaterId;
        const theaterName = item.prfplcnm;
        setLoading(true); 
       try {
            const res = await axios.get(`${API_BASE_URL}/api/theater/getSeatMap?theaterId=${encodeURIComponent(theaterId)}`);
            navigate(`/theater/${theaterId}`, { state: { seatData: res.data, theaterName: theaterName } });
        } catch (error) {
            console.error("좌석 데이터 요청 실패", error);
            alert("좌석 데이터를 불러오는 데 실패했습니다.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading />;

    return (
        <div id="ranksCarouselBox">
            <div className="mainInner2">
            <h1 className="title1">뮤지컬 주간 랭킹</h1>
            <p className="theaterInfoMsg">클릭 시 해당 공연장 좌석 시야를 확인할 수 있습니다.</p>
                <Swiper
                    slidesPerView={5}
                    slidesPerGroup={5}
                    spaceBetween={20}
                    centeredSlides={false}
                    pagination={{
                        clickable: false,
                        dynamicBullets: true,
                    }}
                    loop={true}
                    navigation={true}
                    modules={[Pagination, Navigation]}
                    className="mySwiper"
                >
                    {data?.map((item) => (
                        <SwiperSlide key={item.id}>
                            <div className="musicalBox"
                                 onClick={() => handleTheaterClick(item)}
                                 style={{ cursor: 'pointer' }}
                            >
                                <div className="posterBox">
                                    <MusicalItem key={item?.id} musical={item} />
                                </div>
                                <div className="musicalInfoBox" >
                                    <strong className="musicalName">{item.title}</strong>
                                    <span>{item.prfplcnm}</span>
                                    <span>{item.prfpd}</span>
                                </div>
                            </div>
                        </SwiperSlide>
                    ))}
                </Swiper>
            </div>
        </div>
    )
}

const Slide = styled.div`
    margin:0 20px;
    overflow:"hidden";
    padding:2rem 0;
`;