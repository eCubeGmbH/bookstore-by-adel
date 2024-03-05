import {useEffect, useState} from 'react';
import BooksTable, {Book} from './BooksTable.tsx';
import AuthorsTable, {Author} from './AuthorsTable.tsx';


const MyMainComponent = () => {
    const [bookData, setBookData] = useState<Book[]>([]);
    const [authorData, setAuthorData] = useState<Author[]>([]);

    useEffect(() => {
        // Fetch data from the API endpoint for books
        fetch('/api/books?from=0&to=10')
            .then((response) => {
                if (!response.ok) {
                    throw new Error(response.statusText)
                }
                return response.json() as Promise<Book[]>
            })
            .then((books) =>
                setBookData(books)
            )
            .catch((error) => console.error('Error fetching books:', error))
        // Fetch data from the API endpoint for authors
        fetch('/api/authors?from=0&to=10')
            .then((response) => {
                if (!response.ok) {
                    throw new Error(response.statusText)
                }
                return response.json() as Promise<Author[]>
            })
            .then((authors) =>
                setAuthorData(authors)
            )
            .catch((error) => console.error('Error fetching authors:', error));
    }, []);

    const BooksTableComponent = <BooksTable books={bookData}/>;
    const AuthorsTableComponent = <AuthorsTable authors={authorData}/>;

    return (
        <div>
            {BooksTableComponent}
            {AuthorsTableComponent}
        </div>
    );
};

export default MyMainComponent;