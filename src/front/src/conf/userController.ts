import {PageRequest} from "../sharedComponents/DisplayTable"

export function apiRoleToFrench(role: ApiRole): string {
    switch (role) {
        case "ADMINISTRATOR":
            return "Administrateur";
        case "EDITOR":
            return "Éditeur";
        case "VISUALIZER":
            return "Visualiseur";
        default:
            return "Inconnu";
    }
}

export function frenchToEnglishRole(role: FrenchRole): string {
    switch (role) {
        case "Administrateur":
            return "ADMINISTRATOR";
        case "Éditeur":
            return "EDITOR";
        case "Visualiseur":
            return "VISUALIZER";
        default:
            return "Inconnu";
    }
}

export type ApiRole = "VISUALIZER" | "EDITOR" | "ADMINISTRATOR"
export type FrenchRole = "Administrateur" | "Éditeur" | "Visualiseur"

export type User = {
    id: number,
    email: string,
    lastName: string,
    firstName: string,
    password: string,
    role: ApiRole
}

export type CreateUserDto = {
    email: string,
    lastName: string,
    firstName: string,
    password: string,
    role: ApiRole
}

export async function searchUser(
    size: number = 10,
    page: number = 0,
    filter?: {filterField: string, filterValue: string },
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<User> | undefined> {

    let request = await fetch(window.location.origin + `/api/user/search?size=${size}&page=${page}`)
    if (request.ok) {
        let content = await request.json()
        let user = (content as PageRequest<User>)
        if (user != null) {
            return user
        }
    }
    return undefined
}

export async function createNewUser(user: CreateUserDto) {

    let request = await fetch("/api/user/new",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
    if (request.ok) {
        return true
    } else {
        console.error("Couldn't save the user, error code: " + request.status)
        return false
    }
}