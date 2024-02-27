import { Box, Grid, TextField, Typography } from "@mui/material";
import React from "react";
import InfiniteScroll from "react-infinite-scroll-component";

export type ChargePointModel = {
    id: string,
    name: string,
}

export type TableColumnFilterDefinition = {
    apiField: string,
}

export type TableColumnDefinition = {
    title: string,
    filter: TableColumnFilterDefinition | undefined,
}

export type ConfigurationModel = {
    id: string,
    name: string,
}

export type InfinityScrollItemsTableProps<T> = {
    key: string,
    columns: TableColumnDefinition[],
    data: T[],
    onSelection: (item: T) => void,
    formatter: (item: T, index: number) => JSX.Element
    fetchData: () => T[],
}


export function InfinityScrollItemsTable<T>(props: InfinityScrollItemsTableProps<T>) {
    const [tableData, setTableData] = React.useState(props.data);

    return (
        <Box maxWidth={"true"}>
            <Grid key={"table-header-columns"} container flexDirection={"row"} maxWidth={"true"} justifyItems={"stretch"} >
                {props.columns.map(col => {
                    return (
                        <Grid xs={12/props.columns.length} item key={"table-header-column-" + col.title}>
                            <Typography variant="h6">{col.title}</Typography>
                            {col.filter && (
                                <TextField placeholder={col.title} size="small">
                                </TextField>
                            )}
                        </Grid>
                    )
                })}
            </Grid>
            <Box maxWidth={"true"} border={1} borderColor="gray" paddingTop={2} >
                <div id={props.key+ "scrollableDiv"}                
                style={{
                    maxWidth: "true",
                    height: "60vh",
                    overflow: 'auto',
                    display: 'flex',
                }}>
                    
                        <InfiniteScroll
                                style={{border: 1, borderColor: "black"}}
                                dataLength={tableData.length}
                                next={() => setTableData([...tableData, ...props.fetchData()])}
                                hasMore={true}
                                loader={<h4>Chargement...</h4>}
                                scrollableTarget={props.key  + "scrollableDiv"}
                            >
                                {tableData.map((item: T, index) => props.formatter(item, index))}
                            </InfiniteScroll>
                    
                </div>
            </Box>
        </Box>
    );
}