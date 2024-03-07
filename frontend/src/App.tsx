import {useEffect, useState} from 'react';
import './App.css'
import AuthorsTable, {Author} from './AuthorsTable.tsx';
import Header from './Header.tsx';

const MyMainComponent = () => {
    const [authorData, setAuthorData] = useState<Author[]>([]);
    let page = 0;
    const pageSize = 10;

    const updatePage = (pageNumber: number): void => {
        page = pageNumber;
    };

    useEffect(() => {
        const from: number = page * pageSize;
        const to: number = (page + 1) * pageSize;

        Promise.all([
            fetch(`/api/authors?from=${from}&to=${to}`)
        ])
            .then(([authorsResponse]) => {
                if (!authorsResponse.ok) {
                    throw new Error(authorsResponse.statusText);
                }
                return Promise.all([authorsResponse.json()]);
            })
            .then(([authors]) => {
                setAuthorData(authors);
            })
            .catch((error) => console.error('Error fetching data:', error));
    }, []);

    return (
        <>
            <Header title="Authors"/>
            <AuthorsTable authors={authorData} nextPage={2} prevPage={1}/>

        </>
    );
};

export default MyMainComponent;