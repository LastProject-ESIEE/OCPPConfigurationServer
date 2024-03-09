import { IconButton } from "@mui/material";
import { useNavigate } from "react-router";
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';

// TODO : Delete top props after rework CSS of CreateFirmware page, CreateConfiguration page and CreateChargepoint page
function BackButton(props: {
    link: string
    top?: number
}) {
    const navigate = useNavigate();
    let valueTop;
    if (props.top !== undefined) {
        valueTop = props.top + 2;
    } else {
        valueTop = 2;
    }

    return (
        <IconButton
            onClick={() => navigate(props.link)}
            sx={{
                position: "fixed",
                marginTop: valueTop,
                marginLeft: 2
            }}
        >
            <ArrowBackIosNewIcon/>
        </IconButton>
    )
}

export default BackButton;