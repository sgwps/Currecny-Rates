# Currecny-Rates

Ссылка для скачивания: https://t.me/sgwps_1

Информация по Приложению «Currency Rates». Версия 1.0

1. Основная информация
- Требуемая версия Android – 8.0
- Языки: английский
- Размер: 4 МБ
-Ориентация экрана: портретная

2. Цели и задачи приложения
Целью разработки данного приложения является получение курсов ведущих валют мира и их истории. Основной задачей приложения является получение курсов валют Европейского Центрального банка, последующие задачи — возможность работать с графиком и сохранять данные по курсам.

3. Портрет целевой аудитории
Целевой аудиторией приложения являются люди, часто имеющие необходимость получить курс валют или посмотреть его динамику: сотрудники финансовой сферы, брокеры, путешественники.

4. Структура Приложения и алгоритмы взаимодействия Пользователя с Приложением
 
Приложение состоит из четырех фрагментов, переключающиеся через меню в нижней части экрана (Bottom Navigation Bar):

4. 1. Основной фрагмент
Данный фрагмент позволяет пользователю отравить запрос и увидеть основную информацию по нему. Фрагмент состоит из кнопок, вызывающих список валют, кнопок, вызывающих диалог для выбора дат (DatePickerDialog), строки для ввода суммы. Также отображается абсолютный курс и перевод заданной суммы в валюту запроса на даты начала и окончания периода, отображается разница между значениями.

4. 2. Фрагмент График

В данном фрагменте отображается линейный график курса, соответствующий запросу. Также отображаются максимальный, минимальный курсы на периоде и их даты, средний показатель курсов и курс на дату, заданную пользователем через выбор точки на графике.

4. 3. Фрагмент Динамика

Данный фрагмент отображает курсы заданных валют за 14 дней, предшествующих настоящей дате.

4. 4. Фрагмент Запросы

В данном фрагменте списком представлены сохраненные пользовательские запросы, показана краткая информация про них. Если при сохранении дата окончания периода была датой того дня, конец периода данного запроса всегда будет настоящей датой. При нажатии на элемент списка в остальных фрагментах устанавливаются параметры запроса. В правом нижнем углу экрана расположена кнопка, при  нажатии которой в список добавляется активный на данный момент запрос.


5. Алгоритм работы с приложением

Пользователь, получив доступ к четырем фрагментам (п 4.1 — 4.4), может работать с Приложением, свободно переключаясь между ними.

6. Дизайн приложения.

Дизайн приложения выполнен в минималистичном стиле, с частичным соответствия стандартам Material Design. Предусмотрена темная тема.




































Приложение №1

Основные понятия, используемые в тексте

Запрос — совокупность данных, содержащих исходную валюту и валюту результата, даты начала и окончания периода, сумму исходной валюты. В коде программы представлен классом со статическими переменными.

Приложение №2

Используемые пакеты сервисы

1. MP Android Chart (построение графика)

Copyright 2020 Philipp Jahoda

Licensed under the Apache License, Version 2.0 (the "License");
You may not use this software except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.

2. exchangerate.host (сервис для получения курсов валют)

MIT License
Copyright (c) 2020 Amr Shawky

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


