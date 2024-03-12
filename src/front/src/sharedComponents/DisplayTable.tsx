import { Box, Grid, MenuItem,Skeleton, Select, TextField, Typography, Button } from "@mui/material";
import InfiniteScroll from "react-infinite-scroll-component";
import { FilterOrder, SearchFilter, SearchSort, TableSortType } from "../conf/backendController";
import { useState } from "react";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ArrowDropUpIcon from '@mui/icons-material/ArrowDropUp';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

const scrollListElement = "scrollableDiv"

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
    totalElement: number,
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

    const updateFilter = (newFilters: SearchFilter[]) => {
        if(props.onFiltering){
            // Retrieve all filters without filtered fields
            var currentFilters = filters.filter(prevFilter => !newFilters.find(o => o.filterField === prevFilter.filterField))
            const updatedFilters = [...currentFilters, ...newFilters]
            setFilters(updatedFilters)
            props.onFiltering(updatedFilters, sortField === "" ? undefined : {field: sortField, order: sortOrder})
        }
    }

    const resetScroll = () => {
        document.getElementById(scrollListElement)?.scrollTo(0,0)
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
                                                            resetScroll()
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
                                                <Box>
                                                    <TableColumnFilter
                                                        column={column}
                                                        onFilterValidate={newfilters => {
                                                            if(column.filter){
                                                                resetScroll()
                                                                updateFilter(newfilters)
                                                            }
                                                        }}
                                                    />
                                                </Box>
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
                <div 
                    id={scrollListElement}
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
                                        <Box textAlign={"center"} marginTop={5}>
                                            <img alt="Icône d'erreur" src="/assets/icone-erreur.png" style={{maxWidth: "15%"}}/>
                                            <Typography variant="h6" color={"red"}>{props.error}</Typography>
                                        </Box>
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
                            scrollableTarget={scrollListElement}
                        >
                        {props.data.map((item: T, index) => props.formatter(item, index))}
                    </InfiniteScroll>
                    {(props.data.length === 0) && !props.hasMore && (
                        <Box textAlign={"center"}>
                            <img alt="Icône pour pas d'éléments dans la recherche" src="/assets/icone-noelement.png" style={{maxWidth: "40%"}}/>
                            <Typography variant="h6" color={"black"}>Aucun élément ne correspond à votre demande</Typography>
                        </Box>
                    )}
                </div>
            </Box>
        </Box>
    );
}


function TableColumnFilter(props: {column: TableColumnDefinition, onFilterValidate: (filters: SearchFilter[]) => void}){
    const [firstFilterValue, setFirstFilterValue] = useState(props.column.filter?.filterType === "select" ? DEFAULT_FILTER_SELECT_VALUE : "");
    const [secondFilterValue, setSecondFilterValue] = useState(props.column.filter?.filterType === "select" ? DEFAULT_FILTER_SELECT_VALUE : "");
    const [previousValue, setPreviousValue] = useState(props.column.filter?.filterType === "select" ? DEFAULT_FILTER_SELECT_VALUE : "")
    const [filterOrder, setFilterOrder] = useState<FilterOrder>("=")

    // Format data to backend expected format
    const dateFormatter = (value: string, begindate: boolean) => {
        let selectedDate = new Date(value)
        if(!begindate){
            selectedDate.setHours(23)
            selectedDate.setMinutes(59)
            selectedDate.setSeconds(59)
            selectedDate.setMilliseconds(999)
        }
        return selectedDate.toISOString().replace("Z", "")
    }

    return (
        <Grid container maxWidth={"true"} justifyContent={"center"}>
            {/*Display input filter*/}
            {(props.column.filter?.filterType === "input") && (
                <TextField
                placeholder={props.column.filter.disable ? "Indisponible" : props.column.title}
                disabled={props.column.filter.disable}
                size="small"
                onKeyDown={event => {
                    if(event.key === 'Enter'){
                        if(previousValue !== firstFilterValue){
                            props.onFilterValidate([{filterField: props.column.filter?.apiField ?? "", filterValue: firstFilterValue, filterOrder: filterOrder}])
                            setPreviousValue(firstFilterValue)
                        }
                    }
                }}
                onBlur={() => {
                    if(previousValue !== firstFilterValue){
                        props.onFilterValidate([{filterField: props.column.filter?.apiField ?? "", filterValue: firstFilterValue, filterOrder: filterOrder}])
                        setPreviousValue(firstFilterValue)
                    }
                }}
                onChange={event => {
                    setFirstFilterValue(event.target.value)
                }}
            />
            )}
            {/*Display select input*/}
            {(props.column.filter?.filterType === "select" && (props.column.filter?.restrictedValues?.length ?? 0) > 0) && (
                <Select
                    size="small"
                    variant="outlined"
                    disabled={props.column.filter.disable}
                    fullWidth
                    value={firstFilterValue}
                    onChange={event => {
                        let value = event.target.value
                        setFirstFilterValue(value)
                        if(props.column.filter?.apiValueFormatter){
                            props.onFilterValidate([{filterField: props.column.filter?.apiField ?? "", filterValue: props.column.filter.apiValueFormatter(value), filterOrder: filterOrder}])
                            return
                        }
                        props.onFilterValidate([{filterField: props.column.filter?.apiField ?? "", filterValue: value, filterOrder: filterOrder}])
                    }}>
                    {props.column.filter.restrictedValues && props.column.filter.restrictedValues.map((item) => {
                        let selected = firstFilterValue === item;
                        return (
                            <MenuItem key={"columnfilterOption-"+ item} value={item} selected={selected}>{item}</MenuItem>
                        )}
                    )}
            </Select>
            )}
            {/*Display date input*/}
            {(props.column.filter?.filterType === "date") && (
                <Box>
                    <Grid container wrap="nowrap" flexDirection={"row"} alignContent={"center"} >
                        <Grid item >
                            <Select
                                autoWidth
                                size="small"
                                variant="outlined"
                                disabled={props.column.filter.disable}
                                value={filterOrder}
                                onChange={event => {
                                    let value: FilterOrder = event.target.value as FilterOrder
                                    if(!value){
                                        console.error("Unrecognized filter order.")
                                        return
                                    }
                                    setFilterOrder(value)
                                    if(value === "="){
                                        if(firstFilterValue !== "" && secondFilterValue !== ""){
                                            props.onFilterValidate([
                                                {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(firstFilterValue,true), filterOrder: value},
                                                {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(secondFilterValue,false), filterOrder: value},
                                            ])
                                        }
                                        return
                                    }
                                    if(firstFilterValue !== ""){
                                        props.onFilterValidate([
                                            {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(firstFilterValue,true), filterOrder: value},
                                        ])
                                    }
                                }}
                            >
                            <MenuItem value={"="}>{"="}</MenuItem>
                            <MenuItem value={">"}>{">"}</MenuItem> 
                            <MenuItem value={"<"}>{"<"}</MenuItem> 
                        </Select>
                        </Grid>
                        <Grid item>
                            <LocalizationProvider
                                dateAdapter={AdapterDayjs}
                            >
                                <DatePicker
                                    value={firstFilterValue}
                                    onChange={(newValue) => {
                                        setFirstFilterValue(newValue ?? "")
                                        if(filterOrder === "="){
                                            if(newValue && newValue !== "" && secondFilterValue !== ""){
                                                props.onFilterValidate([
                                                    {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(newValue,true), filterOrder: ">"},
                                                    {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(secondFilterValue,false), filterOrder: "<"},
                                                ])
                                            }
                                            return
                                        }
                                        if(newValue && newValue !== ""){
                                            props.onFilterValidate([
                                                {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(newValue,true), filterOrder: filterOrder},
                                            ])
                                            return
                                        }
                                        if(newValue && newValue === ""){
                                            props.onFilterValidate([
                                                {filterField: props.column.filter?.apiField ?? "", filterValue: "", filterOrder: filterOrder},
                                            ])
                                        }
                                    }}
                                    format="DD/MM/YYYY"
                                    slotProps={{textField: { size: 'small', error: false}}}
                                />
                            </LocalizationProvider>
                        </Grid>
                        <Grid item>
                            {filterOrder === "=" && (
                                <LocalizationProvider
                                    dateAdapter={AdapterDayjs}
                                >
                                    <DatePicker
                                        value={secondFilterValue}
                                        onChange={(newValue) => {
                                            setSecondFilterValue(newValue ?? "")
                                            if(newValue && newValue !== "" && firstFilterValue !== ""){
                                                props.onFilterValidate([
                                                    {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(firstFilterValue,true), filterOrder: ">"},
                                                    {filterField: props.column.filter?.apiField ?? "", filterValue: dateFormatter(newValue,false), filterOrder: "<"},
                                                ])
                                            }
                                            return
                                        }}
                                        format="DD/MM/YYYY"
                                        slotProps={{textField: { size: 'small', error: false}}}
                                    />
                                </LocalizationProvider>
                            )}
                        </Grid>
                    </Grid>
                </Box>
            )}
        </Grid>
    )
}
