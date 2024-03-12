import { Box } from "@mui/material";
import { useParams } from "react-router-dom";
import CreateFirmware from "./CreateFirmware";

export default function EditFirmwarePage() {
    // Get id from router parameter
    const { id } = useParams();
    let firmwareId = id ? Number(id) : undefined;
    
    return (
        <Box maxWidth={"true"}>
            <Box>
                <CreateFirmware id={firmwareId}/>
            </Box>
        </Box>
    );
}