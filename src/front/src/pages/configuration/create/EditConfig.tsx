import { Box } from "@mui/material";
import { useParams } from "react-router-dom";
import CreateConfig from "./CreateConfig";

export function ConfigurationEditPage() {
    // Get id from router parameter
    const { id } = useParams();
    //const [error, setError] = React.useState<string | undefined>(undefined);

    let configurationId = id ? Number(id) : undefined;
    return (
        <Box maxWidth={"true"}>
            <Box>
                <CreateConfig id={configurationId}/>
            </Box>
        </Box>
    );
}

