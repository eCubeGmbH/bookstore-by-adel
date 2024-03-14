import React, { useEffect, useState } from 'react';
import BooksTable from '../src/BooksTable';
import AuthorsTable from '../src/AuthorsTable';

const MyMainComponent = () => {
    const [bookData, setBookData] = useState([]);
    const [authorData, setAuthorData] = useState([]);

    useEffect(() => {
        // Fetch data from the API endpoint for books
        fetch('/api/books?from=0&to=10')
            .then((response) => response.json())
            .then((data) => setBookData(data))
            .catch((error) => console.error('Error fetching books:', error));

        // Fetch data from the API endpoint for authors
        fetch('/api/authors?from=0&to=10')
            .then((response) => response.json())
            .then((data) => setAuthorData(data))
            .catch((error) => console.error('Error fetching authors:', error));
    }, []);

    const BooksTableComponent = <BooksTable books={bookData} />;
    const AuthorsTableComponent = <AuthorsTable authors={authorData} />;

    return (
        <div>
            {BooksTableComponent}
            {AuthorsTableComponent}
        </div>
    );
};

export default MyMainComponent;