import React from "react";
import styled from "styled-components";
import Poster from './Poster';

/**
 * 상단 슬라이더 리스트 아이템
 */

export default function MusicalItem ({musical}) {

    return (
        <ItemWrapper>
            <Poster
                url={musical?.poster || ""}
                musicalId={musical?.id || "UNKNOWN_ID"}
            />
        </ItemWrapper>
    )
}

const ItemWrapper = styled.div`
    width: 200px;
    height: 300px;
    margin: 70px auto 0;
`