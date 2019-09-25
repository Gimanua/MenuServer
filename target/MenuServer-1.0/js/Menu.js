export default class Menu{
    constructor(json){
        this.year = json.year;
        this.week = json.week;
        this.meals = json.meals;
    }

    static getThisWeeksMenu(){
        return fetch('http://localhost:8080/MenuServer/resources/menu')
            .then(response => response.json())
            .then(data => new Menu(data));
    }

    static isToday(day){
        const today = new Date().getDay();
        return this.getDayInteger(day) === today;
    }

    static getDayInteger(day){
        switch(day){
            case 'Måndag':
                return 1;
            case 'Tisdag':
                return 2;
            case 'Onsdag':
                return 3;
            case 'Torsdag':
                return 4;
            case 'Fredag':
                return 5;
            default:
                return -1;
        }
    }
}