import { Box, Grid, MenuItem,Skeleton, Select, TextField, Typography, Button } from "@mui/material";
import InfiniteScroll from "react-infinite-scroll-component";
import { FilterOrder, SearchFilter, SearchSort, TableSortType } from "../conf/backendController";
import { useState } from "react";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ArrowDropUpIcon from '@mui/icons-material/ArrowDropUp';

export const DEFAULT_FILTER_SELECT_VALUE = "Aucun"

export type TableColumnFilterDefinition = {
    apiField: string,
    filterType: "input" | "select" | "date",
    disable?: boolean, // set if the input is disabled or not, allow to display input without filtering functionality 
    restrictedValues?: string[], // must be set for select input
    apiValueFormatter?: (value: string) => string, // must be set for select input to transform input value to api value
}

export type TableColumnSortDefinition = {
    apiField: string
}

export type TableColumnDefinition = {
    title: string,
    size?: number,
    filter?: TableColumnFilterDefinition,
    sort?: TableColumnSortDefinition,
}

export type PageRequest<T> = {
    total: number,
    page: number,
    size: number,
    data: T[],
    next: string
}

export type InfinityScrollItemsTableProps<T> = {
    key: string, // table key
    columns: TableColumnDefinition[], // table columns definition
    data: T[], // items displayed in the table
    hasMore: boolean, // boolean that check if there is no more element
    error: string | undefined,
    onSelection?: (item: T) => void, // function call when an element is selected in the list
    formatter: (item: T, index: number) => JSX.Element // function that transform an item to a JSX element
    fetchData: (filters: SearchFilter[], sort?: SearchSort) => void, // function that fetch next items
    onFiltering?: (filters: SearchFilter[], sort?: SearchSort) => void, // function call when filters changed
}


export function InfinityScrollItemsTable<T>(props: InfinityScrollItemsTableProps<T>) {
    const [filters, setFilters] = useState<SearchFilter[]>([]);
    const [sortField, setSortField] = useState("");
    const [sortOrder, setSortOrder] = useState<TableSortType>("asc");
    const updateFilter = (filterField: string, filterValue: string) => {
        if(props.onFiltering){
            // If filter not present add it
            if(!filters.find(v => v.filterField === filterField)){
                let newFilters = [...filters, {filterField: filterField ?? "", filterValue: filterValue}]
                setFilters(newFilters)
                props.onFiltering(newFilters, sortField === "" ? undefined : {field: sortField, order: sortOrder})
                return
            }
            // Otherwise update filter
            setFilters(prevFilters => {
                const newValues = prevFilters.map(filter => {
                    if(filter.filterField === filterField){
                        return {...filter, filterValue: filterValue}
                    }
                    return filter
                })

                // As function is async props.onFiltering should be call here
                if(props.onFiltering){
                    props.onFiltering(newValues, sortField === "" ? undefined : {field: sortField, order: sortOrder})
                }
                return newValues
            })
        }
    }

    return (
        <Box maxWidth={"true"} paddingTop={1} marginLeft={2} marginBottom={2}>
            <Box marginRight={2} marginBottom={1}>
                {/*Display table columns*/}
                <Grid key={"table-header-columns"} container flexDirection={"row"} maxWidth={"true"}>
                    {props.columns.map(column => {
                        return (
                            <Grid xs={column?.size ?? 12/props.columns.length} item key={"table-header-column-" + column.title} overflow={"hidden"}>
                                <Grid container flexDirection={"column"} justifyContent={"center"}>
                                    <Grid item>
                                        <Grid container justifyContent={"center"}>
                                            <Grid item>
                                                {column.title !== "" && (
                                                <Button
                                                    style={{height: 30}}
                                                    variant="text" 
                                                    onClick={() => {
                                                        if(column.sort){
                                                            let order: TableSortType = sortOrder === "asc" ? "desc": "asc"
                                                            setSortField(column.sort.apiField)
                                                            setSortOrder(order)
                                                            if(props.onFiltering){
                                                                props.onFiltering(filters, {field: column.sort.apiField, order: order})
                                                            }
                                                        }
                                                    }}
                                                    >
                                                    <Grid container wrap="nowrap" flexDirection={"row"} justifyContent={"center"}>
                                                        <Grid item>
                                                            <Typography noWrap variant="body1">{column.title}</Typography>
                                                        </Grid>
                                                        {column.sort && sortField === column.sort.apiField && (
                                                            <Grid item >
                                                                {sortOrder === "asc" && (
                                                                    <ArrowDropDownIcon fontSize="small"/>
                                                                )}
                                                                {sortOrder === "desc" && (
                                                                    <ArrowDropUpIcon fontSize="small"/>
                                                                )}
                                                            </Grid>
                                                        )}
                                                    </Grid>
                                                </Button>
                                                )}
                                            </Grid>
                                        </Grid>
                                    </Grid>  
                                    <Grid item>
                                        <Box>
                                            {column.filter && (
                                                <TableColumnFilter
                                                    column={column}
                                                    onFilterValidate={filterValue => {
                                                        if(column.filter){
                                                            updateFilter(column.filter.apiField, filterValue)
                                                        }
                                                    }}
                                                />
                                            )}
                                        </Box>
                                    </Grid>
                                </Grid>
                            </Grid>
                        )
                    })}
                </Grid>
            </Box>
             {/*Display table content*/}
            <Box key={"box-items-scrollable-list"} maxWidth={"true"} marginRight={2} marginLeft={2}>
                <div id={"scrollableDiv"}
                key={props.key + "-scrollableDiv-list"}
                style={{
                    height: "70vh",
                    overflow: 'auto',
                }}>
                    <InfiniteScroll
                            key="scrollable-items-list"
                            style={{overflow:"hidden", border: 1, borderColor: "black", maxWidth: "true"}}
                            dataLength={props.data.length}
                            next={() => props.fetchData(filters)}
                            hasMore={props.hasMore}
                            loader={
                                <>
                                    {(props.error !== undefined) && (
                                        <Typography variant="h6" color={"red"} textAlign={"center"}>{props.error}</Typography>
                                    )}
                                    {(props.error === undefined) && (
                                        <Box key={"box-skeleton-list"} maxWidth={"true"} marginRight={2} marginLeft={2}>
                                            <Box display="flex" flexDirection="column">
                                                {Array.from(Array(30).keys()).map((_, index) => (
                                                    <Box key={"skeleton-list-" + index} marginY={1}>
                                                        <Skeleton sx={{borderRadius: 50}} variant="rectangular" width={"100%"} height={"5vh"} />
                                                    </Box>
                                                ))}
                                            </Box>
                                        </Box>
                                    )}
                                </>
                            }
                            scrollableTarget={"scrollableDiv"}
                        >
                        {props.data.map((item: T, index) => props.formatter(item, index))}
                    </InfiniteScroll>
                    {(props.data.length === 0) && !props.hasMore && (
                        <Typography variant="h6" color={"black"} textAlign={"center"}>Aucun élément...</Typography>
                    )}
                </div>
            </Box>
        </Box>
    );
}


function TableColumnFilter(props: {column: TableColumnDefinition, onFilterValidate: (value: string, order?: FilterOrder) => void}){
    const [filterValue, setFilterValue] = useState(props.column.filter?.filterType === "select" ? DEFAULT_FILTER_SELECT_VALUE : "");
    const [previousValue, setPreviousValue] = useState(props.column.filter?.filterType === "select" ? DEFAULT_FILTER_SELECT_VALUE : "")
    //const [filterOrder, setFilterOrder] = useState<FilterOrder>("=")
    return (
        <Grid container maxWidth={"true"} justifyContent={"center"}>
            {(props.column.filter?.filterType === "input") && (
                <TextField
                placeholder={props.column.title}
                disabled={props.column.filter.disable}
                size="small"
                onKeyDown={event => {
                    if(event.key === 'Enter'){
                        if(previousValue !== filterValue){
                            props.onFilterValidate(filterValue)
                            setPreviousValue(filterValue)
                        }
                    }
                }}
                onBlur={() => {
                    if(previousValue !== filterValue){
                        props.onFilterValidate(filterValue)
                        setPreviousValue(filterValue)
                    }
                }}
                onChange={event => {
                    setFilterValue(event.target.value)
                }}
            />
            )}
            {(props.column.filter?.filterType === "select" && (props.column.filter?.restrictedValues?.length ?? 0) > 0) && (
                <Select
                    size="small"
                    variant="outlined"
                    disabled={props.column.filter.disable}
                    fullWidth
                    value={filterValue}
                    onChange={event => {
                        let value = event.target.value
                        setFilterValue(value)
                        if(props.column.filter?.apiValueFormatter){
                            props.onFilterValidate(props.column.filter.apiValueFormatter(value))
                            return
                        }
                        props.onFilterValidate(value)
                    }}>

                    {props.column.filter.restrictedValues && props.column.filter.restrictedValues.map((item) => {
                        let selected = filterValue === item;
                        return (
                            <MenuItem key={"columnfilterOption-"+ item} value={item} selected={selected}>{item}</MenuItem>
                        )}
                    )}
            </Select>
            )}
            {(props.column.filter?.filterType === "date") && (
                <Box>
                    
                </Box>
            )}
        </Grid>
    )
}
