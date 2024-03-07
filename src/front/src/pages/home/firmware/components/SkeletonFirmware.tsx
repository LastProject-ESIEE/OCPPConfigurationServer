import {Box, Grid, Skeleton} from "@mui/material";

export function SkeletonFirmware() {
    return (
        <>
            <Grid item xs={12} md={6}>
                <Box>
                    <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                              variant="rounded"/>
                    <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                              variant="rounded"/>
                    <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                              variant="rounded"/>
                </Box>
            </Grid>
            <Grid item xs={12} md={6}>
                <Skeleton sx={{height: "20vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                          variant="rounded"/>
            </Grid>
        </>
    )
}