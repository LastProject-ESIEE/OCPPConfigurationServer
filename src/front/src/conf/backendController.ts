import { PageRequest } from "../sharedComponents/DisplayTable"

function filterOrderToAPI(order: FilterOrder) {
    switch (order) {
        case "<":
            return "<";
        case ">":
            return ">";
        case "=":
            return ":";
        default:
            console.error("Unmanaged filter order type.")
    }
}

export type FilterOrder = "=" | ">" | "<"

export type TableSortType = "desc" | "asc"

export type SearchFilter = { filterField: string, filterValue: string, filterOrder?: FilterOrder }

export type SearchSort = { field: string, order: TableSortType }

export type SearchElementsParameters = {
    size: number,
    page: number,
    filters?: SearchFilter[],
    sort?: SearchSort,
}


// Fetch elements from backend using pagination, filters and sorting
export async function searchElements<T>(path: string, params?: SearchElementsParameters): Promise<PageRequest<T> | undefined> {

    let formattedRequest = path
    if (params) {
        formattedRequest += `?size=${params.size}`
        formattedRequest += `&page=${params.page}`
        if (params.filters) {
            formattedRequest += "&request=" + encodeURI(params.filters.map(filter => {
                let separator = filterOrderToAPI(filter.filterOrder ? filter.filterOrder : "=")
                return `${filter.filterField}${separator}\`${filter.filterValue}\``
            }).join(","))
        }
        if (params.sort) {
            formattedRequest += `&sortBy=${params.sort.field}&order=${params.sort.order}`
        }
    }
    let request = await fetch(formattedRequest)
    if (request.ok) {
        let content = await request.json()
        let response = (content as PageRequest<T>)
        if (response) {
            return response
        }
    }
    return undefined
}

// Fetch all elements from the backend
export async function getAllElements<T>(path: string): Promise<T[] | undefined> {
    let request = await fetch(path)
    if(request.ok){
        let content = await request.json()
        let elements = content as T[]
        if(elements){
            return elements
        }else{
            console.error("Failed to fetch elements :", content)
        }
    }else{
        console.error("Fetch elements failed, error code:" +  request.status)
    }
    return undefined
}

// Fetch an element by id
export async function getElementById<T>(path: string, id: number) {
    let request = await fetch(`${path}/${id}`)
    if(request.ok){
        let content = await request.json()
        let element = (content as T)
        if(element != null){
            return element
        }else{
            console.error("Fetch element failed " + content)
        }
    }else{
        console.error("Fetch element failed, error code:" +  request.status)
    }
    return undefined
}
