import { Box } from "@mui/material";
import { useParams } from "react-router-dom";
import CreateChargepoint from "./CreateChargepoint";

function ChargepointEditPage() {
    // Get id from router parameter
    const { id } = useParams();

    let chargepointId = id ? Number(id) : undefined;
    return (
        <Box maxWidth={"true"}>
            <Box>
                <CreateChargepoint id={chargepointId}/>
            </Box>
        </Box>
    );
}

export default ChargepointEditPage;