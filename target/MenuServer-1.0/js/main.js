import Menu from './Menu.js';

Menu.getThisWeeksMenu().then(menu => {
    document.getElementById('date').innerHTML = `Vecka ${menu.week}, ${menu.year}`;

    for(let i = 0; i < menu.meals.length; i++){
        const meal = menu.meals[i];
        const dayName = getDayFromInteger(i);
        const day = document.createElement('li');
        day.style.listStyleType = 'none';
        
        if (Menu.isToday(dayName))
            day.style.backgroundColor = '#cfe3d4';

        const dayHeader = getElementWithContent('h3', dayName);
        const dishes = getDishes(meal);

        day.appendChild(dayHeader);
        day.appendChild(dishes);

        document.getElementById('menu').appendChild(day);
    }
});

function getDayFromInteger(i){
    switch(i){
        case 0:
            return 'Måndag';
        case 1:
            return 'Tisdag';
        case 2:
            return 'Onsdag';
        case 3:
            return 'Torsdag';
        case 4:
            return 'Fredag';
        default:
            return 'Error';
    }
}

function getElementWithContent(elementName, content) {
    const element = document.createElement(elementName);
    element.innerHTML = content;
    return element;
}

function getDishes(meal) {
    const dishes = document.createElement('ul');

    const dish = getDish(false, meal);
    const altDish = getDish(true, meal);

    dishes.appendChild(dish);
    dishes.appendChild(altDish);

    return dishes;
}

function getDish(altDish, meal){
    const dish = document.createElement('li');
    const dishName = document.createElement('p');

    dishName.appendChild(getElementWithContent('strong', altDish ? 'Alternativ 2: ' : 'Alternativ 1: '));
    dishName.innerHTML += altDish ? meal.alternativeDish.name : meal.mainDish.name;
    dish.appendChild(dishName);

    return dish;
}