import {useEffect, useState} from 'react';
import './App.css'
import AuthorsTable, {Author} from './AuthorsTable.tsx';
import Header from './Header.tsx';

const MyMainComponent = () => {
    const pageSize = 10;

    const [authorData, setAuthorData] = useState<Author[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [hasNext, setHasNext] = useState<boolean>(true);

    async function fetchAuthors(pageNumber: number): Promise<Author[]> {
        const from: number = pageNumber * pageSize;
        const to: number = (pageNumber + 1) * pageSize + 1;

        console.log(`fetchAuthors: pageNumber: ${pageNumber} - from:${from}, to:${to}`)

        const response = await fetch(`/api/authors?from=${from}&to=${to}`);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    const updateCurrentPage = (pageNumber: number): void => {
        console.log(`updateCurrentPage: ${pageNumber}`)
        fetchAuthors(pageNumber)
            .then(authors => {
                setAuthorData(previousAuthor => previousAuthor.filter(() => false));

                if (authors.length > pageSize) {
                    setAuthorData(() => authors.slice(0, pageSize));
                    setHasNext(true);
                } else {
                    setAuthorData(() => authors);
                    setHasNext(false);
                }
            })
        setCurrentPage(pageNumber);
    };

    useEffect(() => {
            updateCurrentPage(0);
        }, []
    )

    const previousPage: number = currentPage === 0 ? 0 : currentPage - 1;
    const nextPage: number = currentPage + 1;
    return (
        <>
            <Header title="Authors"/>
            <AuthorsTable authors={authorData} nextPage={nextPage} prevPage={previousPage}
                          onUpdateCurrentPage={updateCurrentPage}
                          hasPrevious={currentPage !== 0} hasNext={hasNext}/>
        </>
    );
};
export default MyMainComponent;