
/**
 * Data model definition of a button in the nav bar
 */
export interface ButtonData {
    roles: string[];
    label: string;
    href: string;
    subButtons: ButtonData[];
}

/**
 * Define all nav bar buttons and theirs restrictions based on the user role 
 */
export const buttons: ButtonData[] = [
    {
        roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
        label: "Configuration",
        href: "/configuration",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Afficher/modifier",
                href: "",
                subButtons: []
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Créer",
                href: "/new",
                subButtons: []
            },
        ]
    },
    {
        roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
        label: "Bornes",
        href: "/chargepoint",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Afficher/Modifier",
                href: "",
                subButtons: []
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Créer",
                href: "/new",
                subButtons: []
            },
        ]
    },
    {
        roles: ["ADMINISTRATOR", "EDITOR"],
        label: "Firmware",
        href: "/firmware",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Afficher/Modifier",
                href: "",
                subButtons: []
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Créer",
                href: "/new",
                subButtons: []
            }
        ]
    },
    {
        roles: ["ADMINISTRATOR"],
        label: "Gestion des comptes",
        href: "/account",
        subButtons: [
            {
                roles: ["ADMINISTRATOR"],
                label: "Afficher/Modifier",
                href: "",
                subButtons: []
            },
            {
                roles: ["ADMINISTRATOR"],
                label: "Créer",
                href: "/new",
                subButtons: []
            }
        ]
    },
    {
        roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
        label: "Logs",
        href: "/logs",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Fonctionnel",
                href: "/business",
                subButtons: []
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Technique",
                href: "/technical",
                subButtons: []
            }
        ]
    },
]