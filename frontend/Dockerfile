FROM node:20-alpine

WORKDIR /usr/app

COPY ./*.json /usr/app/
COPY ./index.html /usr/app/index.html
COPY ./vite.config.ts /usr/app/vite.config.ts
COPY src/ /usr/app/src/
COPY public/ /usr/app/public/

RUN npm install
RUN npm run build

CMD ["npm", "run", "preview"]
