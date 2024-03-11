import { Box, Typography } from "@mui/material";

export default function LoadingPage(){
    return (
        <Box maxWidth={"true"} maxHeight={"true"} justifyContent={"center"}>
            <Typography variant='h5' textAlign={"center"}>Chargement des informations...</Typography>
        </Box>
    )
}