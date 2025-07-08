import React, {useEffect, useState} from "react";
import style from "../../assets/css/SearchBar.module.css";
import { FaSearch } from "react-icons/fa";

const SearchBar = ({value}) => {

    const [search, setSearch] = useState("");
    const onChangeSearch = (e) => {
        setSearch(e.target.value);
    };

    useEffect(() => {
        setSearch(value);
    }, [value]);


    return (
        <div className={style.searchBarWrapper}>
            <FaSearch />
            <input
                className={style.searchInput}
                placeholder="극장명을 입력하세요."
                maxLength={20}
                value={search}
                onChange={onChangeSearch}
            />
        </div>
    )
}

export default SearchBar;