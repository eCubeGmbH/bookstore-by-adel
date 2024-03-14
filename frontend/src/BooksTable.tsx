export interface Book {
    id:number;
    authorId:number;
    name:string;
    publishDate:Date;
}
interface Props{
    books:Book[];
}
export default function BooksTable({books}:Props){
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
                        <td>{`${book.publishDate}-${book.publishDate}-${book.publishDate}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};
