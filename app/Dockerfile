FROM node:20-alpine

WORKDIR /usr/app

COPY ./package.json /usr/app/package.json
COPY ./package-lock.json /usr/app/package-lock.json
COPY src/ /usr/app/src/
COPY public/ /usr/app/public/

RUN npm install
RUN npm run build

CMD ["npm", "start"]
