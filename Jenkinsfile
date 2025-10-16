pipeline {
    agent any

    tools {
        jdk 'Java'            // matches the JDK name configured in Jenkins
        allure 'Allure'       // matches the Allure Commandline name you added in Jenkins Tools
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/SanaIftikhar4/OnlineStoreAPIFramework.git'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'gradlew clean test --no-daemon'
            }
        }

        stage('Allure Report') {
            steps {
                // Publish the Allure results
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'build/allure-results']]
                ])
            }
        }

        stage('Archive Results') {
            steps {
                archiveArtifacts artifacts: 'build/reports/**/*.*', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            echo '✅ Pipeline completed. Cleaning up workspace...'
            cleanWs()
        }
    }
}
