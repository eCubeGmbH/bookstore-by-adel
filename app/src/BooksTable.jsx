import React from 'react';

const BooksTable = ({books}) => {
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
                {books.map((book) => (
                    <tr key={book.id}>
                        <td>{book.id}</td>
                        <td>{book.authorId}</td>
                        <td>{book.name}</td>
                        <td>{`${book.publishDate[0]}-${book.publishDate[1]}-${book.publishDate[2]}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default BooksTable;