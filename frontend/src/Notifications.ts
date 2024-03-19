import {Bounce, toast} from "react-toastify";


interface NotificationsProps {
    message: string;
    autoClose: number;
}

export const successNotify = ({message, autoClose = 3000}: NotificationsProps) => {
    toast.success(message, {
        autoClose,
        position: "top-right",
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
    })
}
export const warningNotify = ({message, autoClose = 3000}: NotificationsProps) => {
    toast.warning(message, {
        autoClose,
        position: "top-right",
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
    });
};

export const errorNotify = ({message, autoClose = 3000}: NotificationsProps) => {
    toast.error(message, {
        position: "top-right",
        autoClose,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
    });
};
