import {NavLink} from 'react-router-dom';
import '../assets/main-navigation.css'

export default function Navbar() {
    return <header>
        <nav
            className={"navbar"}>
            <ul>
                <li>
                    <NavLink
                        to="/"
                    >
                        Home
                    </NavLink>
                </li>
                <li>
                    <NavLink
                        to="/authors"
                    >
                        Authors
                    </NavLink>
                </li>

                <li>
                    <NavLink
                        to="/books"
                    >
                        Books
                    </NavLink>
                </li>
            </ul>
        </nav>
    </header>
}
