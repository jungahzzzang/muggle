import React from "react";
import "../assets/css/Home.css"
import { useQuery, useQueryClient } from "@tanstack/react-query";
import Loading from "../components/shared/Loading.js";
import Header from "../components/layout/Header.js";
import RankMusicalApi from "../apis/RankMusicalApi.js"
import RankMusicalList from "../components/musical/RankMusicalList.js";
import { Helmet } from "react-helmet";

const HomePage = () => {

    const queryClient = useQueryClient();
    const {data: rankData, isLoading: isLoadingRank} = useQuery({
        queryKey: ['Musicals', 'BoxOfficeRank'],
        queryFn: RankMusicalApi
    });

    // const {data: monthData, isLoading: isLoadingMonth} = useQuery(
    //       ['Musicals', 'BoxOfficeMonth'],
    //       MonthMusicalApi
    // );

    const isLoading = isLoadingRank;

    if (isLoading) {
        return <Loading />;
    }


    return (
        <div id="container">
            <Helmet>
                <title>뮤글</title>
            </Helmet>
            <Header />
          {rankData? (
                <RankMusicalList data={rankData} />
          ) : null}
        </div>
    )
}

export default HomePage;