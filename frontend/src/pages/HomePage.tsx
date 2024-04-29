import '../assets/home-page.css'
import '../assets/add-links.css'
import {Link} from "react-router-dom";

const HomePage = () => {
    return <>
        <h1 className="homepage-container">Welcome to My Bookstore</h1>
        <p>
            At My Bookstore, we offer a wide selection of books across various genres,
            from bestselling novels to academic textbooks.
        </p>
        <h2>Are you an author?</h2>
        <p>
            You can manually add Your Information and your books to our collection!
        </p>
        <div className="add-links">
            <Link to="/authors" className="add-author-link">Add Your Information</Link>
            <span className="link-separator">  </span>
            <Link to="/books" className="add-book-link">Add Your Books</Link>
        </div>
        <h2>About Us</h2>
        <p>
            Our mission is to provide quality books and excellent service to our customers.
        </p>
        <h2>Contact Us</h2>
        <p>
            If you have any questions or inquiries, please feel free to contact us at:
            <br/>
            Email: info@mybookstore.com
            <br/>
            Phone: 123-456-7890
        </p>
    </>
};
export default HomePage;