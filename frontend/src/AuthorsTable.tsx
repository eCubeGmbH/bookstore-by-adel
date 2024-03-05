export interface Author {
    id: string;
    name: string;
    country: string;
    birthDate: Date;
}
interface Props{
    authors: Author[];
}

export default function AuthorsTable({authors}: Props) {
    return (
        <div>
            <h2>Authors Table</h2>
            <table>
                <thead>
                <tr>
                    <th>Author ID</th>
                    <th>Name</th>
                    <th>Country</th>
                    <th>Birth Date</th>
                </tr>
                </thead>
                <tbody>
                {authors.map((author) => (
                    <tr key={author.id}>
                        <td>{author.id}</td>
                        <td>{author.name}</td>
                        <td>{author.country}</td>
                        <td>{`${author.birthDate}-${author.birthDate}-${author.birthDate}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};
