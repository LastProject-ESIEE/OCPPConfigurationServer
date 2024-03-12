import {Skeleton} from "@mui/material";
import React from "react";

export function SkeletonChargepoint() {
    return (
        <>
            <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                      variant="rounded"/>
            <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                      variant="rounded"/>
            <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                      variant="rounded"/>
            <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                      variant="rounded"/>
            <Skeleton sx={{height: "7vh", p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}
                      variant="rounded"/>
        </>
    )

}