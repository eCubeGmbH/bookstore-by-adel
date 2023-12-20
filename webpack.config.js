const path = require("path");

module.exports = {
    mode: 'development',
    entry: './frontend/index.ts',
    output: {
        clean: true,
        path: path.resolve(__dirname, './src/main/resources/public/dist'),
        filename: 'bundle.ts',
    },
    resolve: {
        extensions: ['.tsx\', \'.ts\', \'.js\', \'.jsx']
    },

    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                exclude: /node_modules/,
                use: 'ts-loader',
            },
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                use: 'babel-loader',
            },
        ],
    },
}