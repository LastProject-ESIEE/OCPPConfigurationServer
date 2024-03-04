import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import React, { useEffect } from "react";
import { Box, Grid, ListItemButton, Tooltip, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import { Configuration, searchConfiguration } from "../../../conf/configurationController";


const PAGE_SIZE = 30; // Max items displayed in the configuration table

const configurationTableColumns: TableColumnDefinition[] = [
    {
        title: "Nom",
    },
    {
        title: "Description",
    },
    {
        title: "Dernière modification",
    },
]

function ConfigurationTable() {
    const [tableData, setTableData] = React.useState<Configuration[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
        searchConfiguration(PAGE_SIZE).then((result: PageRequest<Configuration> | undefined) => {
            if (!result) {
                setError("Erreur lors de la récupération des configurations.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<Configuration> = {
        columns: configurationTableColumns,
        key: "configuration-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        onSelection: configuration => {
            console.log("Selected item : " + configuration.id)
        },
        formatter: (configuration) => {
            return (
                <Box key={"box-configuration-edit-path-" + configuration.id} paddingTop={1} maxWidth={"true"}>
                    <Link key={"chargepoint-edit-path-" + configuration.id} to={{pathname: 'edit/' + configuration.id}}
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
                                <Grid item xs={12 / configurationTableColumns.length} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center" noWrap={true}>{configuration.name}</Typography>
                                </Grid>
                                <Grid item xs={12 / configurationTableColumns.length} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Tooltip title={configuration.description}>
                                        <Typography variant="body1" align="center" noWrap={true}>{configuration.description}</Typography>
                                    </Tooltip>
                                </Grid>
                                <Grid item xs={12 / configurationTableColumns.length} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center" noWrap={true}>{new Date(configuration.lastEdit).toLocaleString()}</Typography>
                                </Grid>
                            </Grid>
                        </ListItemButton>
                    </Link>
                </Box>
            )
        },
        fetchData: () => {
            const nextPage = currentPage + 1;
            searchConfiguration(PAGE_SIZE, nextPage).then((result: PageRequest<Configuration> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des configurations.")
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

export default ConfigurationTable;