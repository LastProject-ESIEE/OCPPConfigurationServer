import { Box, Grid, ListItemButton, Typography } from "@mui/material";
import React, { useEffect } from "react";
import { InfinityScrollItemsTable, InfinityScrollItemsTableProps, PageRequest, TableColumnDefinition } from "./DisplayTable";
import { TechnicalLog, searchTechnicalLog } from "../conf/technicalLogController";
// import { Link } from "react-router-dom";

const PAGE_SIZE = 10; // Max items displayed in the technical log table

const technicalLogTableColumns: TableColumnDefinition[] = [
    {
        title: "Date",
        /*
        filter: {
          apiField: "containsTitle",
          onChange: value => console.log("Filtering on : " + value)
        }
        */
    },
    {
        title: "Composant",
    },
    {
        title: "Log",
    }
]

export function TechnicalLogTable() {
    const [tableData, setTableData] = React.useState<TechnicalLog[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
        searchTechnicalLog(PAGE_SIZE).then((result: PageRequest<TechnicalLog> | undefined) => {
            if(!result){
                setError("Erreur lors de la récupération des logs techniques.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<TechnicalLog> = {
        columns: technicalLogTableColumns,
        key: "technical-log-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        onSelection: technicalLog => { console.log("Selected item : " + technicalLog.id) },
        formatter: (technicalLog) => {
            return (
                <Box key={"box-configuration-edit-path-" + technicalLog.id}  paddingTop={1} maxWidth={"true"}>
                    {/*<Link key={"chargepoint-edit-path-" + technicalLog.id}  to={{ pathname: 'display/' + technicalLog.id}} style={{ textDecoration: 'none', paddingTop: 10 }}>*/}
                        <ListItemButton style={{maxWidth: "true", height:"5vh", padding: 0, paddingTop: 3, borderRadius: 50, color: 'black', backgroundColor: '#E1E1E1'}}>
                            <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                                <Grid item xs={12/technicalLogTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                                    <Typography variant="body1" align="center">{new Date(technicalLog.date).toLocaleString()}</Typography>
                                </Grid>
                                <Grid item xs={12/technicalLogTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                                    <Typography variant="body1" align="center">{technicalLog.component}</Typography>
                                </Grid>
                                <Grid item xs={12/technicalLogTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                                    <Typography variant="body1" align="center">{technicalLog.completeLog}</Typography>
                                </Grid>
                            </Grid>
                        </ListItemButton>
                    {/*</Link>*/}
                </Box>
            )
        },
        fetchData: () => {
            const nextPage = currentPage + 1;
            searchTechnicalLog(PAGE_SIZE,nextPage).then((result: PageRequest<TechnicalLog> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des logs techniques.")
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