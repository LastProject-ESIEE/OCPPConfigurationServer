import { Grid } from "@mui/material";
import Box from "@mui/material/Box";

function Error() {

    return (
        <Box>
            <Grid alignContent={"center"}>
                <Grid xs={12}>
                    <Box textAlign={"center"}>
                        <img src="/assets/logo-brs.png" alt="logo de BRS" style={{maxWidth: "40%"}}/>
                    </Box>
                </Grid>
                <Grid xs={12}>
                    <Box textAlign={"center"}>
                        Error 404
                    </Box>
                </Grid>
                <Grid>
                    <Box textAlign={"center"}>
                        Tu sembles t'être perdu, non ?
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
}

export default Error;
