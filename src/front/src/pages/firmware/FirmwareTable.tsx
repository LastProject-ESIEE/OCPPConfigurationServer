import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../DisplayTable";
import React, { useEffect } from "react";
import { Box, Grid, ListItemButton, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import { Firmware, searchFirmware } from "../../conf/FirmwareController";


const PAGE_SIZE = 10; // Max items displayed in the chargepoint table

const firmwareTableColumns: TableColumnDefinition[] = [
    {
        title: "Version",
        /*
        filter: {
          apiField: "containsTitle",
          onChange: value => console.log("Filtering on : " + value)
        }
        */
    },
    {
        title: "Constructeur",
    },
    {
        title: "URL",
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
                setError("Erreur lors de la récupération des bornes.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<Firmware> = {
        columns: firmwareTableColumns,
        key: "charge-point-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        onSelection: firmware => {
            console.log("Selected item : " + firmware.id)
        },
        formatter: (firmware) => {
            return (
                <Box key={"box-configuration-edit-path-" + firmware.id} paddingTop={1} maxWidth={"true"}>
                    <Link key={"chargepoint-edit-path-" + firmware.id} to={{pathname: 'display/' + firmware.id}}
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
                                <Grid item xs={12 / firmwareTableColumns.length} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center">{firmware.version}</Typography>
                                </Grid>
                                <Grid item xs={12 / firmwareTableColumns.length} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center">{firmware.constructor}</Typography>
                                </Grid>
                                <Grid item xs={12 / firmwareTableColumns.length} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center">{firmware.url}</Typography>
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
                    setError("Erreur lors de la récupération des bornes.")
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
