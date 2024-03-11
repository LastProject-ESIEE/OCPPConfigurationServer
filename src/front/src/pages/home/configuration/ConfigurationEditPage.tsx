import { Box } from "@mui/material";
import { useParams } from "react-router-dom";
import CreateConfig from "./CreateConfig";

function ConfigurationEditPage() {
    // Get id from router parameter
    const { id } = useParams();

    let configurationId = id ? Number(id) : undefined;
    return (
        <Box maxWidth={"true"}>
            <Box>
                <CreateConfig id={configurationId}/>
            </Box>
        </Box>
    );
}

export default ConfigurationEditPage;