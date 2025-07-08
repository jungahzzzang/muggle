import styled from "styled-components";
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay, Navigation, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import MusicalItem from "./MusicalItem";

export default function MonthMusicalList({data}) {

    return (
        <div id="newCarouselBox">
            <h1 className="title1">진행 중인 공연</h1>
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
                        <MusicalItem key={item?.id} musical={item} />
                        <div className="movieInfoBox">
                            <strong className="movieName">{item.prfnm}</strong>
                            <span>{item.fcltynm}</span>
                        </div>
                    </SwiperSlide>
                ))}
            </Swiper>
        </div>
    )
}

const Slide = styled.div`
    margin:0 20px;
    overflow:"hidden";
    padding:2rem 0;
`;