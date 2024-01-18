import React, {useEffect, useState} from 'react';

const BooksTable = () => {
    const [booksData, setBooksData] = useState([]);

    useEffect(() => {
        fetch('/api/books?from=0&to=10')
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log(data); // Log the received data

                setBooksData(data);
            })
            .catch((error) => console.error('Error fetching data:', error));
    }, []); // Empty dependency array ensures the effect runs only once on mount


    return (
        <div>
            <h2>Books Table</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Author ID</th>
                    <th>Name</th>
                    <th>Publish Date</th>
                </tr>
                </thead>
                <tbody>
                {booksData.map((row) => (
                    <tr key={row.id}>
                        <td>{row.id.toString()}</td>
                        <td>{row.authorId}</td>
                        <td>{row.name}</td>
                        <td>{`${row.publishDate[0]}-${row.publishDate[1]}-${row.publishDate[2]}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default BooksTable;