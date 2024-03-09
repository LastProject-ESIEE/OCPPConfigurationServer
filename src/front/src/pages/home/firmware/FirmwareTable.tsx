import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import React, { useEffect } from "react";
import { Box, Grid, ListItemButton, Tooltip, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import { Firmware, searchFirmware } from "../../../conf/FirmwareController";


const PAGE_SIZE = 30; // Max items displayed in the firmware table

const firmwareTableColumns: TableColumnDefinition[] = [
    {
        title: "Version",
        size: 2
    },
    {
        title: "Constructeur",
        size: 2
    },
    {
        title: "Modèles compatibles",
        size: 4
    },
    {
        title: "URL",
        size: 4
    },
]


function FirmwareTable() {
    const [tableData, setTableData] = React.useState<Firmware[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
        searchFirmware(PAGE_SIZE).then((result: PageRequest<Firmware> | undefined) => {
            if (!result) {
                setError("Erreur lors de la récupération des firmwares.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])

    let props: InfinityScrollItemsTableProps<Firmware> = {
        columns: firmwareTableColumns,
        key: "firmware-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        formatter: (firmware) => {
            let arr: string[] = []
            let separator = ""
            firmware.typesAllowed.forEach(type => {
                arr.push(separator + type.type)
                separator = ", "
            })

            return (
                <Box key={"box-configuration-edit-path-" + firmware.id} paddingTop={1} maxWidth={"true"}>
                    <Link key={"firmware-edit-path-" + firmware.id} to={{pathname: 'edit/' + firmware.id}}
                          style={{textDecoration: 'none', paddingTop: 10}}>
                        <ListItemButton style={{
                            maxWidth: "true",
                            height: "5vh",
                            padding: 0,
                            paddingTop: 3,
                            borderRadius: 50,
                            color: 'black',
                            backgroundColor: '#E1E1E1'
                        }}>
                            <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                                <Grid item xs={firmwareTableColumns[0].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center" noWrap={true}>{firmware.version}</Typography>
                                </Grid>
                                <Grid item xs={firmwareTableColumns[1].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center" noWrap={true}>{firmware.constructor}</Typography>
                                </Grid>
                                <Grid item xs={firmwareTableColumns[2].size} maxWidth={"true"} justifyContent={"center"}>
                                    <Tooltip title={arr}>
                                        <Typography variant="body1" align="center" noWrap={true}>{arr}</Typography>
                                    </Tooltip>
                                </Grid>
                                <Grid item xs={firmwareTableColumns[3].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Tooltip title={firmware.url}>
                                        <Typography variant="body1" align="center" noWrap={true}>{firmware.url}</Typography>
                                    </Tooltip>
                                </Grid>
                            </Grid>
                        </ListItemButton>
                    </Link>
                </Box>
            )
        },
        fetchData: () => {
            const nextPage = currentPage + 1;
            searchFirmware(PAGE_SIZE, nextPage).then((result: PageRequest<Firmware> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des firmwares.")
                    return
                }
                setTableData([...tableData, ...result.data])
                setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
            });
            setCurrentPage(nextPage)
        },
    }

    return InfinityScrollItemsTable(props)
}

export default FirmwareTable;
